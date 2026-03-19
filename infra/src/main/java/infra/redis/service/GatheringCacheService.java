package infra.redis.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import infra.repository.dto.jdbc.gathering.MainGatheringsProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@Slf4j
@Service
public class GatheringCacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private final RedissonClient redissonClient;
    private final JdbcTemplate jdbcTemplate;

    private static final String CACHE_KEY = "gatherings:cache";
    private static final String LOCK_KEY = "lock:gatherings:cache";
    private static final Duration CACHE_TTL = Duration.ofMinutes(60);
    private static final Duration SOFT_TTL = Duration.ofMinutes(55);
    private static final long LOCK_LEASE_TIME = 30;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public GatheringCacheService(RedisTemplate<String, String> redisTemplate,
                                  RedissonClient redissonClient,
                                  DataSource dataSource) {
        this.redisTemplate = redisTemplate;
        this.redissonClient = redissonClient;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<MainGatheringsProjection> getOrLoadSimple(Supplier<List<MainGatheringsProjection>> dbLoader) {
        try {
            String cached = redisTemplate.opsForValue().get(CACHE_KEY);
            if (cached != null) {
                return deserialize(cached);
            }
            List<MainGatheringsProjection> result = dbLoader.get();
            redisTemplate.opsForValue().set(CACHE_KEY, serialize(result), CACHE_TTL);
            saveToDbCache(serialize(result));
            return result;
        } catch (Exception e) {
            log.warn("Redis failed, fallback to DB cache", e);
            return getFromDbCacheOrLoad(dbLoader);
        }
    }

    public List<MainGatheringsProjection> getOrLoad(Supplier<List<MainGatheringsProjection>> dbLoader) {
        // 1. Redis 시도
        try {
            String cached = redisTemplate.opsForValue().get(CACHE_KEY);
            if (cached != null) {
                triggerRefreshIfNeeded(dbLoader);
                return deserialize(cached);
            }
        } catch (Exception e) {
            log.warn("Redis read failed, fallback to DB cache", e);
            return getFromDbCacheOrLoad(dbLoader);
        }

        // 2. 캐시 미스 - 락 잡고 로드
        return loadWithLock(dbLoader);
    }

    private void triggerRefreshIfNeeded(Supplier<List<MainGatheringsProjection>> dbLoader) {
        try {
            Long remainTtl = redisTemplate.getExpire(CACHE_KEY, TimeUnit.MINUTES);
            if (remainTtl == null || remainTtl > (CACHE_TTL.toMinutes() - SOFT_TTL.toMinutes())) {
                return;
            }

            // 남은 TTL 5분 이하 → 하나의 스레드만 갱신
            RLock lock = redissonClient.getLock(LOCK_KEY);
            boolean acquired = lock.tryLock(0, LOCK_LEASE_TIME, TimeUnit.SECONDS);
            if (acquired) {
                try {
                    log.info("Cache soft TTL expired, refreshing. remain={}min", remainTtl);
                    List<MainGatheringsProjection> result = dbLoader.get();
                    String json = serialize(result);
                    redisTemplate.opsForValue().set(CACHE_KEY, json, CACHE_TTL);
                    saveToDbCache(json);
                } finally {
                    lock.unlock();
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (Exception e) {
            log.warn("Cache refresh failed", e);
        }
    }

    private List<MainGatheringsProjection> loadWithLock(Supplier<List<MainGatheringsProjection>> dbLoader) {
        RLock lock = redissonClient.getLock(LOCK_KEY);
        try {
            boolean acquired = lock.tryLock(3, LOCK_LEASE_TIME, TimeUnit.SECONDS);
            if (acquired) {
                try {
                    String cached = redisTemplate.opsForValue().get(CACHE_KEY);
                    if (cached != null) {
                        return deserialize(cached);
                    }

                    List<MainGatheringsProjection> result = dbLoader.get();
                    String json = serialize(result);
                    redisTemplate.opsForValue().set(CACHE_KEY, json, CACHE_TTL);
                    saveToDbCache(json);
                    return result;
                } finally {
                    lock.unlock();
                }
            } else {
                String cached = redisTemplate.opsForValue().get(CACHE_KEY);
                if (cached != null) {
                    return deserialize(cached);
                }
                return getFromDbCacheOrLoad(dbLoader);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return getFromDbCacheOrLoad(dbLoader);
        } catch (Exception e) {
            log.warn("Redis failed during loadWithLock, fallback to DB cache", e);
            return getFromDbCacheOrLoad(dbLoader);
        }
    }

    // === DB 캐시 fallback ===

    private void saveToDbCache(String json) {
        try {
            String sql = "INSERT INTO gathering_cache (cache_key, data, expired_at, created_at) " +
                    "VALUES (?, ?, ?, ?) " +
                    "ON DUPLICATE KEY UPDATE data = VALUES(data), expired_at = VALUES(expired_at), created_at = VALUES(created_at)";
            jdbcTemplate.update(sql, CACHE_KEY, json,
                    LocalDateTime.now().plus(CACHE_TTL),
                    LocalDateTime.now());
        } catch (Exception e) {
            log.warn("DB cache save failed", e);
        }
    }

    private List<MainGatheringsProjection> getFromDbCacheOrLoad(Supplier<List<MainGatheringsProjection>> dbLoader) {
        try {
            String sql = "SELECT data FROM gathering_cache WHERE cache_key = ? AND expired_at > NOW() LIMIT 1";
            List<String> results = jdbcTemplate.queryForList(sql, String.class, CACHE_KEY);
            if (!results.isEmpty()) {
                log.info("Serving from DB cache fallback");
                return deserialize(results.get(0));
            }
        } catch (Exception e) {
            log.warn("DB cache read failed", e);
        }
        return dbLoader.get();
    }

    public void evict() {
        try {
            redisTemplate.delete(CACHE_KEY);
        } catch (Exception e) {
            log.warn("Redis evict failed", e);
        }
        try {
            jdbcTemplate.update("DELETE FROM gathering_cache WHERE cache_key = ?", CACHE_KEY);
        } catch (Exception e) {
            log.warn("DB cache evict failed", e);
        }
    }

    private String serialize(List<MainGatheringsProjection> data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cache serialize failed", e);
        }
    }

    private List<MainGatheringsProjection> deserialize(String json) {
        try {
            return objectMapper.readValue(json, new TypeReference<>() {});
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Cache deserialize failed", e);
        }
    }
}

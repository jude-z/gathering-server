package infra.redis.generator;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class IdGenerator {
    private final RedisTemplate<String,String> redisTemplate;
    @Value("${chat-message.id}")
    private String key;

    public Long nextId(){
        return redisTemplate.opsForValue().increment(key);
    }
}

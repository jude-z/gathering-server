package api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration(proxyBeanMethods = false)
@EnableRedisHttpSession
public class SessionConfig {
    //TODO : infra에서 이미 LettuceConnectionFactory 등록
}

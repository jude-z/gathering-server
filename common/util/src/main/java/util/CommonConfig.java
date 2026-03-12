package util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class CommonConfig {

    @Bean
    public ImageUrlConverter imageUrlConverter(@Value("${path}") String path) {
        return new ImageUrlConverter(path);
    }
}

package pl.aswit.theatre.config.jackson;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.fasterxml.jackson.databind.ObjectMapper;

import pl.aswit.theatre.util.json.JacksonUtil;

@Configuration
public class JacksonConfiguration {

    @Bean
    public ObjectMapper objectMapper() {
        return JacksonUtil.objectMapper();
    }
}

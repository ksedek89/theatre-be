package pl.aswit.starter.config.log;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import pl.aswit.starter.config.filter.RequestAndResponseLoggingFilter;

@Configuration
public class RequestLoggingFilterConfig {

    @Bean
    public RequestAndResponseLoggingFilter logFilter() {
        return new RequestAndResponseLoggingFilter();
    }
}

package pl.aswit.starter.config.doc;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi() {
        // prettier-ignore
        return GroupedOpenApi
            .builder()
            .group("public-api")
            .pathsToMatch("/api/**")
            .build();
    }

    @Bean
    public GroupedOpenApi privateApi() {
        // prettier-ignore
        return GroupedOpenApi
            .builder()
            .group("private-api")
            .pathsToMatch("/**")
            .pathsToExclude("/api/**")
            .build();
    }
}

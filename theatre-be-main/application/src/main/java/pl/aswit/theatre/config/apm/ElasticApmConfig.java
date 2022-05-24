package pl.aswit.theatre.config.apm;

import co.elastic.apm.attach.ElasticApmAttacher;
import lombok.Setter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.Map;

@Setter
@Configuration
@ConfigurationProperties(prefix = "elastic.apm")
@ConditionalOnProperty(value = "elastic.apm.config.enabled", havingValue = "true")
public class ElasticApmConfig {

    private Map<String, String> config;

    @PostConstruct
    public void init() {
        ElasticApmAttacher.attach(config);
    }
}

package pl.aswit.starter.config.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;

@Configuration
public class FeignClientConfig {

    @Bean
    public feign.Client feignClient() {
        return new feign.okhttp.OkHttpClient(new OkHttpClient.Builder().build());
    }
}

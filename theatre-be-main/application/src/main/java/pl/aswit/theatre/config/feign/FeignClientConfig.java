package pl.aswit.theatre.config.feign;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.Logger;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.slf4j.Slf4jLogger;
import lombok.val;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.PageableSpringEncoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import pl.aswit.theatre.rest.client.RomaClient;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.util.concurrent.TimeUnit;

@Configuration
public class FeignClientConfig {

    @Autowired
    ObjectFactory<HttpMessageConverters> messageConverters;

    @Autowired
    ObjectMapper objectMapper;

    @Bean
    public feign.Client feignClient() {
        return new feign.okhttp.OkHttpClient(new OkHttpClient.Builder().build());
    }

    @Bean
    public RomaClient lodzTheatreClient(){
        return Feign.builder()
                .decoder(feignDecoder())
                .encoder(feignEncoderPageable())
                .logLevel(feignLoggerLevel())
                .logger(new Slf4jLogger(RomaClient.class))
                .target(RomaClient.class, "https://bilety.teatrroma.pl/");
    }

    private OkHttpClient getUnsafeOkHttpClient(long connectTimeout, long readTimeout) {
        try {
            final TrustManager[] trustAllCerts = new TrustManager[] {
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType){
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType){
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder().readTimeout(readTimeout, TimeUnit.MILLISECONDS).connectTimeout(connectTimeout, TimeUnit.MILLISECONDS);
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager)trustAllCerts[0]);
            builder.hostnameVerifier((hostname, session) -> true);

            return builder.build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL;
    }
    private Encoder feignEncoderPageable() {
        return new PageableSpringEncoder(new SpringEncoder(this.messageConverters));
    }

    private Decoder feignDecoder() {
        val jacksonConverter = new MappingJackson2HttpMessageConverter(objectMapper);
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new SpringDecoder(objectFactory);
    }


}

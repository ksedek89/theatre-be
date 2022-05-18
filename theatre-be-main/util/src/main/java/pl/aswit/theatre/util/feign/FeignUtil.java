package pl.aswit.theatre.util.feign;

import java.util.concurrent.TimeUnit;

import javax.validation.Validation;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.PageableSpringEncoder;
import org.springframework.cloud.openfeign.support.ResponseEntityDecoder;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import feign.Client;
import feign.Feign;
import feign.InvocationHandlerFactory;
import feign.Logger;
import feign.Request;
import feign.Retryer;
import feign.codec.Decoder;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import lombok.val;
import pl.aswit.theatre.util.json.JacksonUtil;

import static java.util.concurrent.TimeUnit.SECONDS;

public class FeignUtil {

    public static <T> T createFeignClient(
        Class<T> clientClass,
        Client feignClient,
        String url,
        int connectTimeout,
        int readTimeout,
        Logger.Level loggerLevel,
        ErrorDecoder errorDecoder,
        ObjectFactory<HttpMessageConverters> messageConverters
    ) {
        val javaxValidator = Validation.buildDefaultValidatorFactory().getValidator();

        return Feign
            .builder()
            .decoder(feignDecoder())
            .encoder(feignEncoderPageable(messageConverters))
            .invocationHandlerFactory(new ValidationInvocationHandlerFactory(new InvocationHandlerFactory.Default(), javaxValidator))
            .logLevel(loggerLevel)
            .errorDecoder(errorDecoder)
            .options(new Request.Options(connectTimeout, TimeUnit.MILLISECONDS, readTimeout, TimeUnit.MILLISECONDS, true))
            .retryer(new Retryer.Default(100, SECONDS.toMillis(1), 1))
            .logger(new FeignLogger(clientClass))
            .client(feignClient)
            .target(clientClass, url);
    }

    private static Decoder feignDecoder() {
        val jacksonConverter = new MappingJackson2HttpMessageConverter(JacksonUtil.objectMapper());
        ObjectFactory<HttpMessageConverters> objectFactory = () -> new HttpMessageConverters(jacksonConverter);
        return new ResponseEntityDecoder(new SpringDecoder(objectFactory));
    }

    private static Encoder feignEncoderPageable(ObjectFactory<HttpMessageConverters> messageConverters) {
        return new PageableSpringEncoder(new SpringEncoder(messageConverters));
    }
}

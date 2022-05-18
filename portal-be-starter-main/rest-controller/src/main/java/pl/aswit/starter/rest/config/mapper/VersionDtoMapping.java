package pl.aswit.starter.rest.config.mapper;

import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Configuration;

import com.github.jmnarloch.spring.boot.modelmapper.PropertyMapConfigurerSupport;

import pl.aswit.starter.model.version.VersionDto;
import pl.aswit.starter.rest.model.version.VersionResponse;

@Configuration
public class VersionDtoMapping extends PropertyMapConfigurerSupport<VersionDto, VersionResponse> {

    @Override
    public PropertyMap<VersionDto, VersionResponse> mapping() {
        return new PropertyMap<>() {
            @Override
            protected void configure() {
                map().setVersion(source.getV());
            }
        };
    }
}

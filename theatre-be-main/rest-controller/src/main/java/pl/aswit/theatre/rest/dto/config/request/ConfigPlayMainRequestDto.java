package pl.aswit.theatre.rest.dto.config.request;

import lombok.Data;

import java.util.List;


@Data
public class ConfigPlayMainRequestDto {
    private List<ConfigPlayRequestDto> playConfig;

}

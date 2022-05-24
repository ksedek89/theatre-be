package pl.aswit.theatre.rest.dto.config.request;

import lombok.Data;

@Data
public class ConfigPlayRequestDto {
    private String playName;
    private boolean playValue;
}

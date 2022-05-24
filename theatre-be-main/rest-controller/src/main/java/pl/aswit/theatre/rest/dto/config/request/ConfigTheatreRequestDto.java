package pl.aswit.theatre.rest.dto.config.request;

import lombok.Data;

@Data
public class ConfigTheatreRequestDto {
    private String theatreCode;
    private boolean theatreValue;
}

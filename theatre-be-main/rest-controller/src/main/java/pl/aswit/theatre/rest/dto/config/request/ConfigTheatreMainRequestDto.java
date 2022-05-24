package pl.aswit.theatre.rest.dto.config.request;

import lombok.Data;

import java.util.List;


@Data
public class ConfigTheatreMainRequestDto {
    private List<ConfigTheatreRequestDto> theatreConfig;

}

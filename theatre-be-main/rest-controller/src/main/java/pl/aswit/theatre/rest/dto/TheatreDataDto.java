package pl.aswit.theatre.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.Set;

@Data
@Builder
public class TheatreDataDto {
    private String name;
    private List<TheaterTermDto> termList;
    private Set<TheaterPlayDto> playSet;
}

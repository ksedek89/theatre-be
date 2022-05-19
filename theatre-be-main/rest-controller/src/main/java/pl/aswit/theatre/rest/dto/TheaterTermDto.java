package pl.aswit.theatre.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Builder
@Data
public class TheaterTermDto {
    private String year;
    private String month;
    private String day;
    private String hour;
    private TheaterPlayDto theaterPlay;

}

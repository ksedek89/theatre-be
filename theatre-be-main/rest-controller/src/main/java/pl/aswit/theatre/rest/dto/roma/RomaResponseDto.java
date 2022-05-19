package pl.aswit.theatre.rest.dto.roma;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RomaResponseDto {
    private String status;
    private DataDto data;
}

package pl.aswit.theatre.rest.dto.roma;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class MonthDto {
    private String pierwszy;
    private String ostatni;
    private String miesiacNumer;
    private String miesiacNazwa;
    private String rok;
    private Object terminy;
}

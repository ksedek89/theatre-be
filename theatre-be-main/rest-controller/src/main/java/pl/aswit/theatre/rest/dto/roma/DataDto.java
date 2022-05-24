package pl.aswit.theatre.rest.dto.roma;

import lombok.Data;

@Data
public class DataDto {
    private MonthDto wybranyMiesiac;
    private MonthDto nastepnyMiesiac;
    private MonthDto poprzedniMiesiac;

}

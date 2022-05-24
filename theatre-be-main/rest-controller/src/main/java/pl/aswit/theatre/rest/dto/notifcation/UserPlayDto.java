package pl.aswit.theatre.rest.dto.notifcation;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class UserPlayDto {
    private String name;
    private String link;
}

package pl.aswit.theatre.rest.dto.register.general;

import lombok.Builder;
import lombok.Data;
import pl.aswit.theatre.rest.dto.StatusResponseDto;

@Builder
@Data
public class GeneralResponseDto<T> {
    private T response;
    private StatusResponseDto status;
}

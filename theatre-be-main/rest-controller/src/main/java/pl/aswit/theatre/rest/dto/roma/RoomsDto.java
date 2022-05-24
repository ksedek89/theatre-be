package pl.aswit.theatre.rest.dto.roma;

import lombok.Data;

import java.util.List;

@Data
public class RoomsDto {
    private List<RoomDetailsDto> duza;
    private List<RoomDetailsDto> nova;
}

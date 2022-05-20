package pl.aswit.theatre.rest.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.rest.client.theatre.TheaterI;
import pl.aswit.theatre.rest.dto.TheatreDataDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TheatreService {
    List<TheaterI> theaterList;


    public List<TheatreDataDto> search() {
        List<TheatreDataDto> theatreDataDtoList = theaterList
                .stream()
                .map(TheaterI::searchPerformances)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        return theatreDataDtoList;
    }
}

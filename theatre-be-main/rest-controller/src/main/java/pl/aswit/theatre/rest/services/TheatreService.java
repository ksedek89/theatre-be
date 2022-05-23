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
        fixDates(theatreDataDtoList);
        saveUnsavedPlays(theatreDataDtoList);
        return theatreDataDtoList;
    }

    private void  fixDates(List<TheatreDataDto> theatreDataDtoList){
        theatreDataDtoList.stream().forEach(e->e.getTermList().stream().forEach(f->f.setDay(String.format("%02d", Integer.valueOf(f.getDay())))));
    }

    private void saveUnsavedPlays(List<TheatreDataDto> theatreDataDtoList) {
    }

}

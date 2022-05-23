package pl.aswit.theatre.rest.client.theatre.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.rest.client.theatre.abstr.PoloniaOchCommon;
import pl.aswit.theatre.rest.client.theatre.TheaterI;
import pl.aswit.theatre.rest.dto.TheatreDataDto;

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.polonia", havingValue = "true")
public class Polonia extends PoloniaOchCommon implements TheaterI {

    @Override
    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        return searchTheaterPlays(theatreDataDto, month, year, "Teatr Polonia", ".color-polonia", ".repertoire__spectacle--red", "https://teatrpolonia.pl/pl/content/pl/repertuar");
    }
}

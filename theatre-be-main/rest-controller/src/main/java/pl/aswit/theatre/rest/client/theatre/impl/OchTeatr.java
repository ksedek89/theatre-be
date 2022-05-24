package pl.aswit.theatre.rest.client.theatre.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.rest.client.theatre.TheaterI;
import pl.aswit.theatre.rest.client.theatre.abstr.PoloniaOchCommon;
import pl.aswit.theatre.rest.dto.TheatreDataDto;

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.ochteatr", havingValue = "true")
public class OchTeatr extends PoloniaOchCommon implements TheaterI {


    @Override
    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        return searchTheaterPlays(theatreDataDto, month, year, "Och teatr", "OCH_TEATR", ".color-och--link", ".repertoire__spectacle", "https://ochteatr.com.pl/pl/content/pl/repertuar");
    }
}

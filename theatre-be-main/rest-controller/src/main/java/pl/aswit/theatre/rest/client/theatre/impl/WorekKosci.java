package pl.aswit.theatre.rest.client.theatre.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.rest.client.theatre.TheaterI;
import pl.aswit.theatre.rest.client.theatre.abstr.GoOut;
import pl.aswit.theatre.rest.dto.TheatreDataDto;

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.worek-kosci", havingValue = "true")
public class WorekKosci extends GoOut implements TheaterI {

    @Override
    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        return addGoOut(theatreDataDto, "Worek kości", "WOREK_KOSCI", "https://goout.net/pl/worek-kosci/vzevpc/");
    }

    @Override
    public String addDescriptions(String link) {
        return super.setDescriptions(link);
    }

}

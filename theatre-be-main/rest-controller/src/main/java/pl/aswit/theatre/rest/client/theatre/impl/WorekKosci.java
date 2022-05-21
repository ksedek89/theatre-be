package pl.aswit.theatre.rest.client.theatre.impl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.rest.client.theatre.TheaterI;
import pl.aswit.theatre.rest.client.theatre.abstr.GoOut;
import pl.aswit.theatre.rest.dto.TheaterPlayDto;
import pl.aswit.theatre.rest.dto.TheaterTermDto;
import pl.aswit.theatre.rest.dto.TheatreDataDto;

import java.util.Iterator;

import static pl.aswit.theatre.util.CalendarUtil.getNumberFromDeclinationName;

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.worek-kosci", havingValue = "true")
public class WorekKosci extends GoOut implements TheaterI {


    @Override
    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        return addGoOut(theatreDataDto, "Worek kości", "https://goout.net/pl/worek-kosci/vzevpc/");
    }
}

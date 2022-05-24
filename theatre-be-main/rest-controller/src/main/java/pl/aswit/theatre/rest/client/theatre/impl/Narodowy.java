package pl.aswit.theatre.rest.client.theatre.impl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.rest.client.theatre.TheaterI;
import pl.aswit.theatre.rest.dto.TheaterPlayDto;
import pl.aswit.theatre.rest.dto.TheaterTermDto;
import pl.aswit.theatre.rest.dto.TheatreDataDto;

import java.util.Iterator;

import static pl.aswit.theatre.util.CalendarUtil.numberOfDays;

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.narodowy", havingValue = "true")
public class Narodowy implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Teatr Narodowy");
            theatreDataDto.setCode("NARODOWY");
            String url = "https://narodowy.pl/repertuar,kalendarium.html?month="+month+"&year="+year;
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".kal-row");
            Iterator<Element> iterator = elements.iterator();
            if (!iterator.hasNext()) {
                return false;
            }
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                prepareListForScene(theatreDataDto, month, year, nextElement, ".kal-1");
                prepareListForScene(theatreDataDto, month, year, nextElement, ".kal-2");
                prepareListForScene(theatreDataDto, month, year, nextElement, ".kal-3");
            }
            return true;
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

    private void prepareListForScene(TheatreDataDto theatreDataDto, String month, String year, Element nextElement, String scene) {
        Elements select = nextElement.select(scene).get(0).select(".kal-date");
        if(select.size() > 0) {
            Iterator<Element> iterator1 = select.iterator();
            while (iterator1.hasNext()) {
                Element next = iterator1.next();
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(year)
                        .month(month)
                        .day(nextElement.previousElementSibling().select(".kal-day").get(0).childNodes().get(0).toString())
                        .hour(next.select("b").get(0).childNodes().get(0).toString())
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .name(next.select("a").get(0).childNodes().get(0).toString())
                                .link("https://narodowy.pl/" + next.select("a").get(0).attr("href"))
                                .build())
                        .build());

            }
        }
    }

}

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

import static pl.aswit.theatre.util.CalendarUtil.getNumberFromName;
import static pl.aswit.theatre.util.CalendarUtil.numberOfDays;

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.buffo", havingValue = "true")
public class Buffo implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Studio Buffo");
            String url = "https://studiobuffo.com.pl/repertuar";
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("tr");
            Iterator<Element> iterator = elements.iterator();
            if (!iterator.hasNext()) {
                return false;
            }

            String day = null;
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                if(nextElement.select("td").get(0).childNodes().size() != 0) {
                    day = nextElement.select("td").get(0).childNodes().get(0).toString().replaceAll("\\D", "");
                }
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(nextElement.parent().parent().previousElementSibling().childNodes().get(0).toString().replaceAll("\\D", ""))
                        .month(getNumberFromName(nextElement.parent().parent().previousElementSibling().childNodes().get(0).toString().replaceAll("\\d", "").trim()))
                        .day(day)
                        .hour(nextElement.select("td").get(1).childNodes().size() > 0 ?
                                nextElement.select("td").get(1).childNodes().get(0).toString() : null)
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .name(nextElement.select("td").get(2).select("a").size() > 0?
                                        nextElement.select("td").get(2).select("a").get(0).childNodes().get(0).toString() : null)
                                .link(nextElement.select("td").get(2).select("a").size() > 0 ?
                                        nextElement.select("td").get(2).select("a").get(0).attr("href"): null)
                                .build())
                        .build());
            }
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

}

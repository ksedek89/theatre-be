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

import static pl.aswit.theatre.util.CalendarUtil.getNumberFromDeclinationName;
import static pl.aswit.theatre.util.CalendarUtil.getNumberFromName;

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.kiepura", havingValue = "true")
public class Kiepura implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Mazowiecki Teatr Muzyczny im. Jana Kiepury");
            theatreDataDto.setCode("KIEPURA");
            String url = "https://www.mteatr.pl/pl/repertuar";
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".col-xs-6.col-sm-4.col-md-15");
            Iterator<Element> iterator = elements.iterator();
            if (!iterator.hasNext()) {
                return false;
            }
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                String date = nextElement.select(".label").get(0).childNodes().get(0).toString().replaceAll("\\n", "");
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(date.replaceAll("\\D", "").substring(2,6))
                        .month(getNumberFromDeclinationName(nextElement.select(".meta-holder").get(0).childNodes().get(0).toString().replaceAll("\\n", "").replaceAll(",.+", "").replaceAll("\\d", "").trim()))
                        .day(nextElement.select(".label").get(0).childNodes().get(0).toString().replaceAll("\\n", "").substring(0,2))
                        .hour(date.replaceAll("\\D", "").substring(6, 8)+":" + date.replaceAll("\\D", "").substring(8,10))
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .link("https://www.mteatr.pl" + nextElement.select("a").attr("href"))
                                .name(nextElement.select(".box_tytul").get(0).select("h2").get(0).childNodes().get(0).toString())
                                .build())
                        .build());
            }
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

}

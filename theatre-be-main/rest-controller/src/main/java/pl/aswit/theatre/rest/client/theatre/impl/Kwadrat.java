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

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.kwadrat", havingValue = "true")
public class Kwadrat implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Teatr Kwadrat");
            theatreDataDto.setCode("KWADRAT");
            String url = "https://bilety.teatrkwadrat.pl/?direction=down&&month="+month+"&year="+year;
            log.info(url);
            Document document = Jsoup.connect(url).get();

            if(!getNumberFromName(document.select(".month").get(0).childNodes().get(0).toString().replaceAll("\\d", "").trim()).equals(month)){
                return false;
            }
            Elements elements = document.select(".spektakl-scena");
            Iterator<Element> iterator = elements.iterator();
            if (!iterator.hasNext()) {
                return false;
            }
            int element = 0;
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                if(nextElement.select("span").size() == 0){
                    continue;
                }
                element++;
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(year)
                        .month(month)
                        .day(nextElement.parent().child(0).select(".dzien-mies").get(0).childNodes().get(0).toString().replaceAll("\\n", ""))
                        .hour(nextElement.select("span").get(0).childNodes().get(0).toString())
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .name(nextElement.select("a").get(0).childNodes().get(0).toString())
                                .link(nextElement.select("a").attr("href"))
                                .build())
                        .build());
            }
            if(element>0){
                return true;
            }
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }


}

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
@ConditionalOnProperty(name = "theatre.capitol", havingValue = "true")
public class Capitol implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Teatr Capitol");
            String url = "https://teatrcapitol.pl/repertuar/?periodFrom="+year+"-"+month+"-01&periodTo="+year+"-"+month+"-"+numberOfDays.get(month);
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".ev");
            Iterator<Element> iterator = elements.iterator();
            if (!iterator.hasNext()) {
                return false;
            }
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(year)
                        .month(month)
                        .day(nextElement.select("h3").get(0).childNodes().get(0).toString())
                        .hour(nextElement.select(".ev-hour").get(0).childNodes().get(0).toString())
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .name(nextElement.select(".main-title").get(0).childNodes().get(0).toString().trim())
                                .link(nextElement.select(".main-title").attr("href"))
                                .build())
                        .build());
            }
            return true;
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

}

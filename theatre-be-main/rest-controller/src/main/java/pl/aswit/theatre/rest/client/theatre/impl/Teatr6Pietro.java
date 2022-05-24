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

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.6pietro", havingValue = "true")
public class Teatr6Pietro implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Teatr 6.piętro");
            theatreDataDto.setCode("6PIETRO");
            String url = "https://bilety.teatr6pietro.pl/index.html?date="+year+"-"+month+"-01";
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".row.row-sm-height");
            Iterator<Element> iterator = elements.iterator();
            if (!iterator.hasNext()) {
                return false;
            }
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(year)
                        .month(nextElement.select(".row.row-sm-height").get(0).select("p").get(0).childNodes().get(0).toString().trim().substring(3,5))
                        .day(nextElement.select(".row.row-sm-height").get(0).select("p").get(0).childNodes().get(0).toString().trim().substring(0,2))
                        .hour(nextElement.select(".row.row-sm-height").get(0).select(".hour").get(0).childNodes().get(0).toString())
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .name(nextElement.select(".row.row-sm-height").get(0).select(".title").get(0).childNodes().get(0).toString())
                                .link(nextElement.select(".row.row-sm-height").get(0).select(".more-link").select("a").attr("href"))
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

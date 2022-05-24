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

import static pl.aswit.theatre.util.CalendarUtil.monthMap;

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.rampa", havingValue = "true")
public class Rampa implements TheaterI {


    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Teatr Rampa");
            theatreDataDto.setCode("RAMPA");
            String url = "https://www.teatr-rampa.pl/repertuar?month=" + year + "-" + month;
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".span4.title");
            Iterator<Element> iterator = elements.iterator();

            if(document.toString().contains("Teatr Rampa nie ma jeszcze repertuaru")){
                return false;
            }
            if(!document.select(".span6.djshows-navi").toString().contains(" " + monthMap.get(month) + " ")){
                return true;
            }

            if (!iterator.hasNext()) {
                return false;
            }

            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(year)
                        .month(month)
                        .day(nextElement.parent().parent().parent().select(".day").get(0).childNodes().get(0).toString().replaceAll("\\n", "").trim())
                        .hour(nextElement.parent().select(".time").get(0).childNodes().get(0).toString())
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .name(nextElement.select("a").get(0).childNodes().get(0).toString())
                                .link("https://www.teatr-rampa.pl" + nextElement.select("a").attr("href"))
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

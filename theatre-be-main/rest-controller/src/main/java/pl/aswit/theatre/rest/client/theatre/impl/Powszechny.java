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
@ConditionalOnProperty(name = "theatre.powszechny", havingValue = "true")
public class Powszechny implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Teatr Powszechny");
            theatreDataDto.setCode("POWSZECHNY");
            String url = "https://www.powszechny.com/index/repertuar.html";
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".repertuar-scena.repertuar-scena-x1.clearfix");
            Iterator<Element> iterator = elements.iterator();
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                if(nextElement.parent().parent().previousElementSibling().previousElementSibling().select(".repertuar-nag-data").size()>0){
                    month = nextElement.parent().parent().previousElementSibling().previousElementSibling().select(".repertuar-nag-data").get(0).childNodes().get(0).toString().replaceAll("\\n", "");
                    month = getNumberFromName(month);
                    year = nextElement.parent().parent().previousElementSibling().previousElementSibling().select(".repertuar-nag-data").get(0).childNodes().get(2).toString().replaceAll("\\n", "");
                }
                //scena duża
                if(nextElement.select(".repertuar-scena-godzina").size() > 0) {
                    addToList(theatreDataDto, month, year, nextElement);
                }
                //scena mała
                nextElement = nextElement.nextElementSibling();
                if(nextElement.select(".repertuar-scena-godzina").size() > 0) {
                    addToList(theatreDataDto, month, year, nextElement);
                }

            }
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

    private void addToList(TheatreDataDto theatreDataDto, String month, String year, Element nextElement) {
        theatreDataDto.getTermList().add(TheaterTermDto
                .builder()
                .year(year)
                .month(month)
                .day(nextElement.parent().select(".dzien").get(0).childNodes().get(0).toString().replaceAll("\\n", ""))
                .hour(nextElement.select(".repertuar-scena-godzina").get(0).childNodes().get(0).toString().replaceAll("\\n", ""))
                .theaterPlay(TheaterPlayDto
                        .builder()
                        .name(nextElement.select(".repertuar-scena-tytul").get(0).childNodes().get(0).toString())
                        .link("https://www.powszechny.com" + nextElement.select(".repertuar-scena-tytul").attr("href"))
                        .build())
                .build());
    }

}

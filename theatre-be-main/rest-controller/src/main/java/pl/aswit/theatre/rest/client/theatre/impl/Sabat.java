package pl.aswit.theatre.rest.client.theatre.impl;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
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

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.sabat", havingValue = "true")
public class Sabat implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Teatr Sabat");
            theatreDataDto.setCode("SABAT");
            String url = "https://www.teatr-sabat.pl/pl/repertuar";
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".therms-body");
            Iterator<Element> iterator = elements.iterator();
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                String dataToRegex = nextElement.select(".info").get(0).childNodes().get(0).toString().replaceAll("\\n", "");
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(dataToRegex.replaceAll("\\D","").substring(2,6))
                        .month(getNumberFromDeclinationName(dataToRegex.replaceAll(".+,.\\d\\d", "").replaceAll("\s", "").replaceAll("\\d.+", "")))
                        .day(dataToRegex.replaceAll(".+,", "").trim().substring(0,2))
                        .hour(dataToRegex.replaceAll("\\D","").substring(6,8) + ":" + dataToRegex.replaceAll("\\D","").substring(8, 10) )
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .name(nextElement.select("a").get(0).childNodes().get(0).toString())
                                .link(nextElement.select("a").attr("href"))
                                .build())
                        .build());
            }
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

    @Override
    public String addDescriptions(String link) {
        try {
            log.info(link);
            Connection connect = Jsoup.connect(link);
            Document document = connect.get();
            return document.select(".description-body").text();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }


}

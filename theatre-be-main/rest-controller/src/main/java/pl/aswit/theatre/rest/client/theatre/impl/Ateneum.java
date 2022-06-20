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

@Service
@Slf4j
@ConditionalOnProperty(name = "theatre.ateneum", havingValue = "true")
public class Ateneum implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Teatr Ateneum");
            theatreDataDto.setCode("ATENEUM");
            String url = "https://teatrateneum.pl/?page_id=26579&data=" + year+"-"+month+"-01";
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".spektakl");
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
                        .day(nextElement.parent().select(".text-left").get(0).childNodes().get(0).toString().trim())
                        .hour(nextElement.select(".spektakl-godzina").get(0).childNodes().get(0).toString().replace("\n", "").trim())
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .name(nextElement.select("h2").get(0).childNodes().get(0).toString())
                                .link(nextElement.select(".tytul-spektaklu").get(0).attr("href"))
                                .build())
                        .build());
            }
            return true;
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
            return document.select(".threecol-two").text();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }


}

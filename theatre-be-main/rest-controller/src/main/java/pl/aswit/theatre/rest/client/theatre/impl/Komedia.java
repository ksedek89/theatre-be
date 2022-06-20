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
@ConditionalOnProperty(name = "theatre.komedia", havingValue = "true")
public class Komedia implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Teatr Komedia");
            theatreDataDto.setCode("KOMEDIA");
            String url = "https://teatrkomedia.pl/repertuar/";
            log.info(url);
            Connection connect = Jsoup.connect(url);
            Document document = connect.get();
            Elements elements = document.select(".showWrapper");
            Iterator<Element> iterator = elements.iterator();
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                TheaterPlayDto theatrePlay = getTheatrePlay(nextElement);
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(year)
                        .month(nextElement.select("em").get(0).childNodes().get(0).toString())
                        .day(nextElement.select("h4").get(0).childNodes().get(0).toString().replace(".", "").trim())
                        .hour(nextElement.select(".hourWrapper").get(0).childNodes().get(0).toString().replaceAll("\ng.", ""))
                        .theaterPlay(theatrePlay)
                        .build());
            }

        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

    private TheaterPlayDto getTheatrePlay(Element nextElement) {
        return TheaterPlayDto
                .builder()
                .name(nextElement.select(".titleWrapper").select("a").get(0).childNodes().get(0).toString().trim().replace("&nbsp;"," "))
                .link("https://teatrkomedia.pl" + nextElement.select(".titleWrapper").select("a").attr("href"))
                .build();
    }

    public String addDescriptions(String link){
        try {
            log.info(link);
            Connection connect = Jsoup.connect(link);
            Document document = connect.get();
            Element element = document.select(".generic").get(0);
            Iterator<Element> p = element.select("p").iterator();
            StringBuilder sb = new StringBuilder();
            while (p.hasNext()) {
                Element next = p.next();
                sb.append(next.childNodes().get(0).toString().replace("&nbsp;", " ")).append(" ");
            }

            return sb.toString();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return null;

    }




}

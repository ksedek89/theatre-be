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
@ConditionalOnProperty(name = "theatre.lodz", havingValue = "true")
public class Lodz implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Teatr Muzyczny w Łodzi");
            theatreDataDto.setCode("TEATR_MUZYCZNY_LODZ");
            String url = "https://www.teatr-muzyczny.lodz.pl/repertuar?month=" + month;
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".single-show-wrapper.active-show");
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
                        .day(nextElement.select(".month-day").get(0).childNodes().get(0).toString())
                        .hour(nextElement.select(".date").select("span").get(0).childNodes().get(0).toString())
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .name(nextElement.select(".show-name").select("a").get(0).childNodes().get(0).toString().trim())
                                .link(nextElement.select(".show-name").select("a").get(0).attr("href").replaceAll("\\?.*+", ""))
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
            Iterator<Element> p = document.select(".rtejustify").iterator();
            StringBuilder sb = new StringBuilder();
            while (p.hasNext()) {
                Element next = p.next();
                sb.append(next.childNodes().get(0).toString()).append(" ");
            }

            return sb.toString();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }

}

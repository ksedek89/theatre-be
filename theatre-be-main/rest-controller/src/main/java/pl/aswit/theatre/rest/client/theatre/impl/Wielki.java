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
@ConditionalOnProperty(name = "theatre.wielki", havingValue = "true")
public class Wielki implements TheaterI {

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try {
            theatreDataDto.setName("Teatr Wielki Opera Narodowa");
            String url = "https://teatrwielki.pl/repertuar/kalendarium/data/"+year+"/"+month+"/";
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".category");
            Iterator<Element> iterator = elements.iterator();
            if (!iterator.hasNext()) {
                return false;
            }
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                if(nextElement.childNodes().size() == 0){
                    continue;
                }
                if(!nextElement.childNodes().get(0).toString().equalsIgnoreCase("Opera")
                        && !nextElement.childNodes().get(0).toString().equalsIgnoreCase("Balet")){
                    continue;
                }
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(year)
                        .month(month)
                        .day(nextElement.parent().parent().parent().parent().parent().select(".day").get(0).childNodes().get(0).toString())
                        .hour(nextElement.parent().select(".hour").get(0).childNodes().get(0).toString())
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .link("https://teatrwielki.pl" +nextElement.parent().parent().select("a").get(0).attr("href").replaceAll("/termin/.+", ""))
                                .name(nextElement.parent().parent().select("a").get(0).childNodes().get(1).toString())
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

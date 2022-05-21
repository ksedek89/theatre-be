package pl.aswit.theatre.rest.client.theatre.abstr;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.aswit.theatre.rest.dto.TheaterPlayDto;
import pl.aswit.theatre.rest.dto.TheaterTermDto;
import pl.aswit.theatre.rest.dto.TheatreDataDto;

import java.util.Iterator;

@Slf4j
public class GoOut {

    public boolean addGoOut(TheatreDataDto theatreDataDto, String name, String url) {
        try {
            theatreDataDto.setName(name);
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".eventCard-name-link");
            Iterator<Element> iterator = elements.iterator();
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                String time = nextElement.parent().select("time").get(0).childNodes().get(0).toString().replaceAll("\\D", "");
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(time.substring(4,8))
                        .month(time.substring(2,4))
                        .day(time.substring(0,2))
                        .hour(time.substring(8,10)+":"+time.substring(10,12))
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .link("https://goout.net" + nextElement.attr("href"))
                                .name(nextElement.select("span").get(0).childNodes().get(0).toString().trim())
                                .build())
                        .build());
            }
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }
}

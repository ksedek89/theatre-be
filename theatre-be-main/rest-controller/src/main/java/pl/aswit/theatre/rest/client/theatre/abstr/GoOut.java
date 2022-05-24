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

    public boolean addGoOut(TheatreDataDto theatreDataDto, String name, String code, String url) {
        try {
            theatreDataDto.setName(name);
            theatreDataDto.setCode(code);
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select(".eventCard-name-link");
            Iterator<Element> iterator = elements.iterator();
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                String time = nextElement.parent().select("time").get(0).attr("datetime");
                theatreDataDto.getTermList().add(TheaterTermDto
                        .builder()
                        .year(time.substring(0,4))
                        .month(time.substring(5,7))
                        .day(time.substring(8, 10))
                        .hour(time.substring(11,16))
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

package pl.aswit.theatre.rest.client.theatre.abstr;


import lombok.extern.slf4j.Slf4j;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import pl.aswit.theatre.rest.dto.TheaterPlayDto;
import pl.aswit.theatre.rest.dto.TheaterTermDto;
import pl.aswit.theatre.rest.dto.TheatreDataDto;

import java.util.Iterator;

@Slf4j
public abstract class PoloniaOchCommon{

    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year, String name, String code, String className, String className1, String urlRequest) {
        try {
            theatreDataDto.setName(name);
            theatreDataDto.setCode(code);
            String monthTo = Integer.valueOf(month) == 12 ? "01" : String.format("%02d", Integer.valueOf(month)+1);
            String url = urlRequest+"?from="+year+"-"+month+"-01T01:00:00Z&to="+year+"-"+monthTo+"-01T01:00:00Z";
            log.info(url);
            Document document = Jsoup.connect(url).get();
            Elements elements = document.select("tr");
            Iterator<Element> iterator = elements.iterator();
            if (!iterator.hasNext()) {
                return false;
            }
            int noOfElements = 0;
            while (iterator.hasNext()) {
                Element nextElement = iterator.next();
                if(nextElement.select("td").size() == 0){
                    continue;
                }
                if(nextElement.select("td").select(className1).size() == 0){
                    continue;
                }
                Elements td = nextElement.select("td").select(className1);
                Iterator<Element> elementsIterator = td.iterator();
                while(elementsIterator.hasNext()){
                    Element next = elementsIterator.next();
                    if(next.select(className).size() == 0){
                        continue;
                    }
                    theatreDataDto.getTermList().add(TheaterTermDto
                            .builder()
                            .year(year)
                            .month(month)
                            .day(nextElement.select("td").get(0).select("span").get(0).childNodes().get(0).toString())
                            .hour(next.childNodes().get(0).toString().replaceAll("\\n", "").trim().substring(0,5))
                            .theaterPlay(TheaterPlayDto
                                    .builder()
                                    .name(next.select(className).get(0).childNodes().get(0).toString())
                                    .link(next.select("span").attr("href"))
                                    .build())
                            .build());
                    noOfElements++;
                }
            }
            if(noOfElements == 0){
                return false;
            }
            return true;
        }catch(Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

    public String addDescriptionsCommon(String link) {
        try {
            log.info(link);
            Connection connect = Jsoup.connect(link);
            Document document = connect.get();
            Iterator<Element> p = document.select(".event__details").get(0).select("div").iterator();
            StringBuilder sb = new StringBuilder();
            while (p.hasNext()) {
                Element next = p.next();
                sb.append(next.text().trim()).append(" ");
            }

            return sb.toString();
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return null;
    }


}

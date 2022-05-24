package pl.aswit.theatre.rest.client.theatre.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.rest.client.RomaClient;
import pl.aswit.theatre.rest.client.theatre.TheaterI;
import pl.aswit.theatre.rest.dto.TheaterPlayDto;
import pl.aswit.theatre.rest.dto.TheaterTermDto;
import pl.aswit.theatre.rest.dto.TheatreDataDto;
import pl.aswit.theatre.rest.dto.roma.RomaResponseDto;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@AllArgsConstructor
@ConditionalOnProperty(name = "theatre.roma", havingValue = "true")
public class Roma implements TheaterI {
    private RomaClient romaClient;

    @Override
    public boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year) {
        try{
            theatreDataDto.setName("Teatr Roma");
            theatreDataDto.setCode("ROMA");
            String url = year + "-" + month + "-01";
            log.info("https://bilety.teatrroma.pl/rezerwacja/termin.html?json=true&jsonType=terminy&od=" + url);
            RomaResponseDto repertoire = romaClient.getRepertoire(url);
            if( repertoire.getData().getWybranyMiesiac().getTerminy() instanceof ArrayList){
                return false;
            }
            LinkedHashMap<String, LinkedHashMap> terminy = (LinkedHashMap<String, LinkedHashMap>) repertoire.getData().getWybranyMiesiac().getTerminy();
            for (Map.Entry<String, LinkedHashMap> entry : terminy.entrySet()) {
                LinkedHashMap value = entry.getValue();
                List duza = ((ArrayList)value.get("duza"));
                if(duza != null){
                    theatreDataDto.getTermList().addAll((ArrayList)duza.stream().map(e -> prepareTheterTermList((LinkedHashMap)e)).collect(Collectors.toList()));
                }
                List nova = ((ArrayList)value.get("nova"));
                if(nova != null){
                    theatreDataDto.getTermList().addAll((ArrayList)nova.stream().map(e -> prepareTheterTermList((LinkedHashMap)e)).collect(Collectors.toList()));
                }
            }
            return true;
        }catch (Exception e){
            log.error(e.getMessage(), e);
        }
        return false;
    }

    private TheaterTermDto prepareTheterTermList(LinkedHashMap e) {
                return TheaterTermDto
                        .builder()
                        .hour((String)e.get("godzina"))
                        .day(e.get("termin_data").toString().substring(8, 10))
                        .month(e.get("termin_data").toString().substring(5, 7))
                        .year(e.get("termin_data").toString().substring(0, 4))
                        .theaterPlay(TheaterPlayDto
                                .builder()
                                .name((String)e.get("nazwa"))
                                .link((String)e.get("opis_wydarzenia_url"))
                                .build())
                        .build();
    }

}

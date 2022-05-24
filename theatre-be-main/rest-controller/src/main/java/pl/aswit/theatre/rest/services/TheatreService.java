package pl.aswit.theatre.rest.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.model.entity.Play;
import pl.aswit.theatre.model.entity.Term;
import pl.aswit.theatre.model.entity.Theatre;
import pl.aswit.theatre.model.repository.PlayRepository;
import pl.aswit.theatre.model.repository.TermRepository;
import pl.aswit.theatre.model.repository.TheatreRepository;
import pl.aswit.theatre.rest.client.theatre.TheaterI;
import pl.aswit.theatre.rest.dto.TheaterTermDto;
import pl.aswit.theatre.rest.dto.TheatreDataDto;

import java.util.*;
import java.util.stream.Collectors;
@Service
@AllArgsConstructor
@Slf4j
public class TheatreService {
    private List<TheaterI> theaterList;
    private TheatreRepository theatreRepository;
    private PlayRepository playRepository;
    private TermRepository termRepository;

    public List<TheatreDataDto> searchNewPerformances() {
        List<TheatreDataDto> theatreDataDtoList = theaterList
                .stream()
                .map(TheaterI::searchPerformances)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
        filterEmptyDate(theatreDataDtoList);
        fixDates(theatreDataDtoList);
        saveUnsavedPlays(theatreDataDtoList);
        return theatreDataDtoList;
    }

    private void filterEmptyDate(List<TheatreDataDto> theatreDataDtoList) {
        theatreDataDtoList.stream().forEach(e->{
            List<TheaterTermDto> termList = e.getTermList();
            termList = termList
                    .stream()
                    .filter(f->f.getHour() != null
                            && f.getDay() != null
                            && f.getMonth() != null
                            && f.getYear() != null
                            && f.getHour().replaceAll(":", "").matches("\\d{4}")
                    )
                    .collect(Collectors.toList());
            e.setTermList(termList);
        });
    }

    private void  fixDates(List<TheatreDataDto> theatreDataDtoList){
        theatreDataDtoList.stream().forEach(e->e.getTermList().stream().forEach(f-> {
            try{
                f.setDay(String.format("%02d", Integer.valueOf(f.getDay())));
                f.setHour(f.getHour().trim());
            }catch (Exception ee){
                System.out.println();
            }
        }
        ));
    }

    private void saveUnsavedPlays(List<TheatreDataDto> theatreDataDtoList) {
        saveNewTheatres(theatreDataDtoList);
        saveAndSetNewPlays(theatreDataDtoList);
        saveNewTerms(theatreDataDtoList);
    }

    private void saveNewTheatres(List<TheatreDataDto> theatreDataDtoList) {
        theatreDataDtoList.stream().forEach(e->{
            Theatre theatre = theatreRepository.findByName(e.getName());
            if(theatre == null){
                Theatre addedTheatre  = theatreRepository.save(Theatre.builder().name(e.getName()).build());
                log.info("Added theatre: " + addedTheatre);
            }
        });
    }

    private void saveAndSetNewPlays(List<TheatreDataDto> theatreDataDtoList) {
        theatreDataDtoList.stream().forEach(e->{
            e.getPlaySet().stream().forEach(f-> {
                Play play = playRepository.findByName(f.getName());
                if(play == null){
                    Play addedPlay = playRepository.save(Play
                            .builder()
                            .name(f.getName())
                            .link(f.getLink())
                            .theatre(theatreRepository.findByName(e.getName()))
                            .build()
                    );
                    log.info("Added play: " + addedPlay);

                }
            });
        });
    }

    private void saveNewTerms(List<TheatreDataDto> theatreDataDtoList) {
        theatreDataDtoList.stream().forEach(e->{
            e.getTermList().stream().forEach(f->{
                Play play = playRepository.findByName(f.getTheaterPlay().getName());
                Date date = prepareDate(f.getHour(), f.getDay(), f.getMonth(), f.getYear());
                Term term = termRepository.findByPlayAndPerformanceDate(play, prepareDate(f.getHour(), f.getDay(), f.getMonth(), f.getYear()));
                if(term == null){
                    Term addedTerm = termRepository.save(Term
                            .builder()
                            .play(play)
                            .performanceDate(date)
                            .build());
                    log.info("Added term: "+ addedTerm);
                }
            });
        });
    }

    private Date prepareDate(String hour, String day, String month, String year){
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, Integer.valueOf(year));
        cal.set(Calendar.MONTH, Integer.valueOf(month) -1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.valueOf(day));
        cal.set(Calendar.HOUR_OF_DAY, Integer.valueOf(hour.substring(0,2)));
        cal.set(Calendar.MINUTE, Integer.valueOf(hour.substring(3,5)));
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

}

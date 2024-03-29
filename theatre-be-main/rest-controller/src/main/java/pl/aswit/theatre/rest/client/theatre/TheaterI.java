package pl.aswit.theatre.rest.client.theatre;


import pl.aswit.theatre.rest.dto.TheatreDataDto;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

public interface TheaterI {

    default TheatreDataDto searchPerformances(){
        TheatreDataDto theatreDataDto = TheatreDataDto.builder().playSet(new HashSet<>()).termList(new ArrayList<>()).build();
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int year = Calendar.getInstance().get(Calendar.YEAR);
        while (searchTheaterPlays(theatreDataDto,String.format("%02d", month), String.valueOf(year))) {
            if (month == 12) {
                month = 1;
                year++;
            } else {
                month++;
            }
        }



        theatreDataDto.getTermList().forEach(theaterTermDto->{
            if(!theatreDataDto.getPlaySet().contains(theaterTermDto.getTheaterPlay())){
                theatreDataDto.getPlaySet().add(theaterTermDto.getTheaterPlay());
            }
        });

        prepareDescription(theatreDataDto);

        return theatreDataDto;
    }

    default void prepareDescription(TheatreDataDto theatreDataDto){
        theatreDataDto.getPlaySet().stream().filter(e->e.getLink() != null && e.getLink().length()>0).forEach(e->e.setDescription(addDescriptions(e.getLink())));
    }

    boolean searchTheaterPlays(TheatreDataDto theatreDataDto, String month, String year);

    String addDescriptions(String link);
}

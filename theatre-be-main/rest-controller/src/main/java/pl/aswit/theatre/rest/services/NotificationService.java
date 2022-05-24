package pl.aswit.theatre.rest.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.model.entity.*;
import pl.aswit.theatre.model.repository.*;
import pl.aswit.theatre.rest.dto.config.request.ConfigPlayMainRequestDto;
import pl.aswit.theatre.rest.dto.config.request.ConfigTheatreMainRequestDto;
import pl.aswit.theatre.rest.dto.config.request.ConfigTheatreRequestDto;
import pl.aswit.theatre.rest.dto.register.general.GeneralResponseDto;
import pl.aswit.theatre.rest.dto.register.request.RegisterUserRequestDto;
import pl.aswit.theatre.rest.dto.register.response.RegisterUserResponseDto;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {
    UserRepository userRepository;
    PlayRepository playRepository;
    TheatreRepository theatreRepository;
    UserTheatreRepository userTheatreRepository;
    UserPlayRepository userPlayRepository;

    @Transactional
    public GeneralResponseDto updateUserTheatrePreferences(String email, ConfigTheatreMainRequestDto configTheatreMainRequestDto) {
        User user = userRepository.findByEmail(email);
        configTheatreMainRequestDto.getTheatreConfig().forEach(e->{
            Theatre theatre = theatreRepository.findByCode(e.getTheatreCode());
            if(user == null || theatre == null){
                return;
            }
            UserTheatre userTheatre = userTheatreRepository.findByTheatreAndUser(theatre, user);
            if(userTheatre != null){
                userTheatre.setActive(e.isTheatreValue());
                userTheatre.setStatusChangeDate(new Date());
            }else{
                userTheatre = UserTheatre.builder().user(user).statusChangeDate(new Date()).theatre(theatre).active(e.isTheatreValue()).build();
            }
            userTheatreRepository.save(userTheatre);
        });
        return GeneralResponseDto.builder().build();
    }

    public GeneralResponseDto updateUserPlayPreferences(String email, ConfigPlayMainRequestDto configPlayMainRequestDto) {
        User user = userRepository.findByEmail(email);
        configPlayMainRequestDto.getPlayConfig().forEach(e->{
            Play play = playRepository.findByName(e.getPlayName());
            if(user == null || play == null){
                return;
            }
            UserPlay userPlay = userPlayRepository.findByPlayAndUser(play, user);
            if(userPlay != null){
                userPlay.setActive(e.isPlayValue());
                userPlay.setStatusChangeDate(new Date());
            }else{
                userPlay = UserPlay.builder().user(user).statusChangeDate(new Date()).active(e.isPlayValue()).play(play).build();
            }
            userPlayRepository.save(userPlay);
        });
        return GeneralResponseDto.builder().build();
    }
}

package pl.aswit.theatre.rest.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.model.entity.*;
import pl.aswit.theatre.model.repository.*;
import pl.aswit.theatre.rest.dto.config.request.ConfigPlayMainRequestDto;
import pl.aswit.theatre.rest.dto.config.request.ConfigPlayRequestDto;
import pl.aswit.theatre.rest.dto.config.request.ConfigTheatreMainRequestDto;
import pl.aswit.theatre.rest.dto.notifcation.UserPlayDto;
import pl.aswit.theatre.rest.dto.register.general.GeneralResponseDto;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
                userTheatre.setActive(e.getTheatreValue());
                userTheatre.setStatusChangeDate(new Date());
            }else{
                userTheatre = UserTheatre.builder().user(user).statusChangeDate(new Date()).theatre(theatre).active(e.getTheatreValue()).build();
            }
            userTheatreRepository.save(userTheatre);
        });
        return GeneralResponseDto.builder().build();
    }

    public GeneralResponseDto updateUserPlayPreferences(String email, ConfigPlayMainRequestDto configPlayMainRequestDto) {
        User user = userRepository.findByEmail(email);
        for (ConfigPlayRequestDto e : configPlayMainRequestDto.getPlayConfig()) {
            Play play = playRepository.findByName(e.getPlayName());
            if (user == null || play == null) {
                continue;
            }
            UserPlay userPlay = userPlayRepository.findByPlayAndUser(play, user);
            if (userPlay != null) {
                userPlay.setActive(e.getPlayValue());
                userPlay.setStatusChangeDate(new Date());
            } else {
                userPlay = UserPlay.builder().user(user).statusChangeDate(new Date()).active(e.getPlayValue()).play(play).build();
            }
            userPlayRepository.save(userPlay);
        }
        return GeneralResponseDto.builder().build();
    }

    public GeneralResponseDto prepareMessages() {
        List<User> allByActive = userRepository.findAllByActive(true);
        allByActive.stream().forEach(e->{
            List<UserPlay> userPlayList= userPlayRepository.findAllByUser(e);
            List<UserPlayDto> userPlayDtoList = new ArrayList();
            userPlayList.stream().forEach(f->{
                if(f.isActive()
                        && f.getStatusChangeDate().compareTo(e.getCreateDate()) < 1
                        && !f.isNotificationSent()){
                    Play play = f.getPlay();
                    userPlayDtoList.add(UserPlayDto.builder().link(play.getLink()).name(play.getLink()).build());
                    f.setNotificationSent(true);
                    userPlayRepository.save(f);
                }
                sendNotification(e, userPlayDtoList);
            });

        });
        return GeneralResponseDto.builder().build();
    }

    private void sendNotification(User user, List<UserPlayDto> userPlayDtoList) {
        log.info(user.toString());
        log.info(userPlayDtoList.toString());
        //send mail
    }

    public GeneralResponseDto sendNewsletter(String email) {
        List<Play> allPlays = playRepository.findAll();

        return GeneralResponseDto.builder().build();
    }
}

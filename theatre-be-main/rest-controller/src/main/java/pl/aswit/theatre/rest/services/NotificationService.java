package pl.aswit.theatre.rest.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.model.entity.Theatre;
import pl.aswit.theatre.model.entity.User;
import pl.aswit.theatre.model.entity.UserTheatre;
import pl.aswit.theatre.model.repository.TheatreRepository;
import pl.aswit.theatre.model.repository.UserRepository;
import pl.aswit.theatre.model.repository.UserTheatreRepository;
import pl.aswit.theatre.rest.dto.config.request.ConfigTheatreMainRequestDto;
import pl.aswit.theatre.rest.dto.config.request.ConfigTheatreRequestDto;
import pl.aswit.theatre.rest.dto.register.general.GeneralResponseDto;
import pl.aswit.theatre.rest.dto.register.request.RegisterUserRequestDto;
import pl.aswit.theatre.rest.dto.register.response.RegisterUserResponseDto;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Slf4j
public class NotificationService {
    UserRepository userRepository;
    TheatreRepository theatreRepository;
    UserTheatreRepository userTheatreRepository;

    @Transactional
    public GeneralResponseDto updateUserTheatrePreferences(String email, ConfigTheatreMainRequestDto configTheatreMainRequestDto) {
        User user = userRepository.findByEmail(email);
        configTheatreMainRequestDto.getTheatreConfig().forEach(e->{
            Theatre theatre = theatreRepository.findByCode(e.getTheatreCode());
            UserTheatre userTheatre = userTheatreRepository.findByTheatreAndUser(theatre, user);
            if(userTheatre != null){
                userTheatre.setActive(e.isTheatreValue());
            }else{
                userTheatre = UserTheatre.builder().user(user).theatre(theatre).active(e.isTheatreValue()).build();
            }
            userTheatreRepository.save(userTheatre);
        });
        return GeneralResponseDto.builder().build();
    }
}

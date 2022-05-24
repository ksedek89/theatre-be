package pl.aswit.theatre.rest.services;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import pl.aswit.theatre.model.entity.Play;
import pl.aswit.theatre.model.entity.Term;
import pl.aswit.theatre.model.entity.Theatre;
import pl.aswit.theatre.model.entity.User;
import pl.aswit.theatre.model.repository.PlayRepository;
import pl.aswit.theatre.model.repository.TermRepository;
import pl.aswit.theatre.model.repository.TheatreRepository;
import pl.aswit.theatre.model.repository.UserRepository;
import pl.aswit.theatre.rest.client.theatre.TheaterI;
import pl.aswit.theatre.rest.dto.TheaterTermDto;
import pl.aswit.theatre.rest.dto.TheatreDataDto;
import pl.aswit.theatre.rest.dto.register.general.GeneralResponseDto;
import pl.aswit.theatre.rest.dto.register.request.RegisterUserRequestDto;
import pl.aswit.theatre.rest.dto.register.response.RegisterUserResponseDto;

import javax.validation.Valid;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class UserService {
    private UserRepository userRepository;

    public GeneralResponseDto<RegisterUserResponseDto> registerUser(RegisterUserRequestDto registerUserRequestDto) {
        if(userRepository.findByEmail(registerUserRequestDto.getEmail()) == null){
            userRepository.save(User
                    .builder()
                    .active(false)
                    .email(registerUserRequestDto.getEmail())
                    .name(registerUserRequestDto.getName())
                    .surname(registerUserRequestDto.getSurname())
                    .build());
        }
        //send notification
        return GeneralResponseDto.<RegisterUserResponseDto>builder().build();
    }
}

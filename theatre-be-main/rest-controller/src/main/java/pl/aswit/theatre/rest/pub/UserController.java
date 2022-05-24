package pl.aswit.theatre.rest.pub;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.aswit.theatre.rest.dto.register.general.GeneralResponseDto;
import pl.aswit.theatre.rest.dto.register.request.RegisterUserRequestDto;
import pl.aswit.theatre.rest.dto.register.response.RegisterUserResponseDto;
import pl.aswit.theatre.rest.services.UserService;

import javax.validation.Valid;


@RestController
@RequestMapping("${pru.servlet.pub-api-path}/user")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping("/register")
    public GeneralResponseDto<RegisterUserResponseDto> registerUser(@Valid @RequestBody RegisterUserRequestDto registerUserRequestDto) {
        return userService.registerUser(registerUserRequestDto);
    }
}

package pl.aswit.theatre.rest.pub;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import pl.aswit.theatre.rest.dto.config.request.ConfigPlayMainRequestDto;
import pl.aswit.theatre.rest.dto.config.request.ConfigTheatreMainRequestDto;
import pl.aswit.theatre.rest.dto.register.general.GeneralResponseDto;
import pl.aswit.theatre.rest.dto.register.response.RegisterUserResponseDto;
import pl.aswit.theatre.rest.services.NotificationService;

import javax.validation.Valid;


@RestController
@RequestMapping("${pru.servlet.pub-api-path}/notification")
@AllArgsConstructor
public class NotificationController {
    private NotificationService notificationService;

    @PostMapping("/{email}/config/theatre")
    public GeneralResponseDto<RegisterUserResponseDto> updateUserTheatrePreferences(@PathVariable("email") String email, @RequestBody ConfigTheatreMainRequestDto configTheatreMainRequestDto) {
        return notificationService.updateUserTheatrePreferences(email, configTheatreMainRequestDto);
    }

    @PostMapping("/{email}/config/play")
    public GeneralResponseDto<RegisterUserResponseDto> updateUserPlayPreferences(@PathVariable("email") String email, @RequestBody ConfigPlayMainRequestDto configTheatreMainRequestDto) {
        return notificationService.updateUserPlayPreferences(email, configTheatreMainRequestDto);
    }
}

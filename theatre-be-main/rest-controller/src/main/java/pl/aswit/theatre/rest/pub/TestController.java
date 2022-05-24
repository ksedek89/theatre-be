package pl.aswit.theatre.rest.pub;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.aswit.theatre.rest.dto.register.general.GeneralResponseDto;
import pl.aswit.theatre.rest.services.NotificationService;


@RestController
@RequestMapping("${pru.servlet.pub-api-path}/test")
@AllArgsConstructor
public class TestController {
    private  NotificationService notificationService;
    @PostMapping("/test")
    public GeneralResponseDto prepareMessages() {
        return notificationService.prepareMessages();
    }

}

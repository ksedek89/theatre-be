package pl.aswit.theatre.rest.pub;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.aswit.theatre.rest.dto.TheatreDataDto;
import pl.aswit.theatre.rest.services.TheatreService;

import java.util.List;

@RestController
@RequestMapping("${pru.servlet.pub-api-path}/theatre")
@AllArgsConstructor
public class TheatreController {
    private TheatreService theatreService;

    @GetMapping("/find-new")
    public List<TheatreDataDto> searchNewPerformances() {
        return theatreService.searchNewPerformances();
    }
}

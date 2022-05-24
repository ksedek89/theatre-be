package pl.aswit.theatre.rest.pub;

import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.aswit.theatre.business.version.VersionService;
import pl.aswit.theatre.rest.model.version.VersionResponse;

@Slf4j
@RestController("VersionControllerPub")
@RequestMapping("${pru.servlet.pub-api-path}")
public class VersionController {

    private final VersionService versionService;
    private final ModelMapper mapper;

    public VersionController(VersionService versionService, ModelMapper mapper) {
        this.versionService = versionService;
        this.mapper = mapper;
    }

    @GetMapping("/version")
    public VersionResponse getVersion() {
        return mapper.map(versionService.getVersion(), VersionResponse.class);
    }
}

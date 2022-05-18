package pl.aswit.starter.business.version;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import pl.aswit.starter.model.version.VersionDto;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VersionServiceImpl implements VersionService {

    private final String version;

    public VersionServiceImpl(@Value("${app.version}") String version) {
        this.version = version;
    }

    @Override
    public VersionDto getVersion() {
        return new VersionDto(version);
    }
}

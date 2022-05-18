package pl.aswit.starter.rest.model.version;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class VersionResponse {

    @NotEmpty
    private String version;
}

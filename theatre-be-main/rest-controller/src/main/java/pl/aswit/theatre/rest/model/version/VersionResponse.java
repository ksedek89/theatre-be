package pl.aswit.theatre.rest.model.version;

import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Data
public class VersionResponse {

    @NotEmpty
    private String version;
}

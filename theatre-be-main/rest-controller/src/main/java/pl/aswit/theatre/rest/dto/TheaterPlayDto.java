package pl.aswit.theatre.rest.dto;

import lombok.Builder;
import lombok.Data;

import java.util.Objects;

@Builder
@Data
public class TheaterPlayDto {
    private String name;
    private String link;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TheaterPlayDto that = (TheaterPlayDto) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}

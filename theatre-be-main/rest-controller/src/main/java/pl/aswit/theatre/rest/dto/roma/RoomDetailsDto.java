package pl.aswit.theatre.rest.dto.roma;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RoomDetailsDto {
    private String tid;
    private String tidentyfikator;
    private String dzienTygodnia;
    private String godzina;
    @JsonProperty("termin_data")
    private String terminData;
    private String nazwa;
    private String wolne;
    private String wolneKasa;
    private String wolneKasa1;
    private String dostepneRezerwacja;
    private String dostepneSprzedaz;
    private String wydarzenieId;
    @JsonProperty("opis_wydarzenia_url")
    private String opisWydarzeniaUrl;
    private String opisDodatkowy;
    private String urlSprzedaz;
    private String urlRezerwacja;
    private String rodzajGrupyWydarzen;
    private String sprzedazDostepneOd;
    private String rezerwacjaDostepneOd;
    private String obsada;
}

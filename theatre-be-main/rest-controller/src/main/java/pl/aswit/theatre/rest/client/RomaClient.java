package pl.aswit.theatre.rest.client;


import feign.Param;
import feign.RequestLine;
import pl.aswit.theatre.rest.dto.roma.RomaResponseDto;

public interface RomaClient {
    @RequestLine("GET /rezerwacja/termin.html?json=true&jsonType=terminy&od={dateFrom}")
    RomaResponseDto getRepertoire(@Param("dateFrom") String dateFrom);
}

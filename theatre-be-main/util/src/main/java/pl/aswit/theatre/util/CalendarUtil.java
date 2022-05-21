package pl.aswit.theatre.util;

import java.util.Map;

public class CalendarUtil {
    public final static Map<String, String> monthMap = Map.ofEntries(
            Map.entry("01", "styczeń"),
            Map.entry("02", "luty"),
            Map.entry("03", "marzec"),
            Map.entry("04", "kwiecień"),
            Map.entry("05", "maj"),
            Map.entry("06", "czerwiec"),
            Map.entry("07", "lipiec"),
            Map.entry("08", "sierpień"),
            Map.entry("09", "wrzesień"),
            Map.entry("10", "październik"),
            Map.entry("11", "listopad"),
            Map.entry("12", "grudzień")
    );

    public final static Map<String, String> monthMapDeclination = Map.ofEntries(
            Map.entry("01", "stycznia"),
            Map.entry("02", "lutego"),
            Map.entry("03", "marca"),
            Map.entry("04", "kwietnia"),
            Map.entry("05", "maja"),
            Map.entry("06", "czerwca"),
            Map.entry("07", "lipca"),
            Map.entry("08", "sierpnia"),
            Map.entry("09", "września"),
            Map.entry("10", "października"),
            Map.entry("11", "listopada"),
            Map.entry("12", "grudnia")
    );

    public static String getNumberFromName(String name){
        return monthMap.entrySet().stream().filter(e->e.getValue().equalsIgnoreCase(name)).findFirst().get().getKey();
    }

    public static String getNumberFromDeclinationName(String name){
        return monthMapDeclination.entrySet().stream().filter(e->e.getValue().equalsIgnoreCase(name)).findFirst().get().getKey();
    }
    public final static Map<String, Integer> numberOfDays = Map.ofEntries(
            Map.entry("01", 31),
            Map.entry("02", 28),
            Map.entry("03", 31),
            Map.entry("04", 30),
            Map.entry("05", 31),
            Map.entry("06", 30),
            Map.entry("07", 31),
            Map.entry("08", 31),
            Map.entry("09", 30),
            Map.entry("10", 31),
            Map.entry("11", 30),
            Map.entry("12", 31)
    );

}

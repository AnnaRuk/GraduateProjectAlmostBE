package de.ait.gp.utils;

import java.time.format.DateTimeFormatter;

public class TimeDateFormatter {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss:S");

    private TimeDateFormatter() {
    }

}

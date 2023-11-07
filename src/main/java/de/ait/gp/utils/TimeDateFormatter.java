package de.ait.gp.utils;

import java.time.format.DateTimeFormatter;

public class TimeDateFormatter {
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private TimeDateFormatter() {
    }

}

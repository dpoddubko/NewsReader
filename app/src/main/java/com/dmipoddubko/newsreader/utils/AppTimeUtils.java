package com.dmipoddubko.newsreader.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class AppTimeUtils {

    private final static DateTimeFormatter FORMATTER = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    private AppTimeUtils() {
        // hidden
    }

    public static synchronized String now() {
        return toString(DateTime.now());
    }

    public static synchronized DateTime fromString(String value) {
        return value == null ? null : DateTime.parse(value, FORMATTER);
    }

    public static synchronized String toString(DateTime value) {
        return value == null ? null : FORMATTER.print(value);
    }
}

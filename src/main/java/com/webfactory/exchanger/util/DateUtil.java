package com.webfactory.exchanger.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DateUtil {

    private static final String DATE_PATTERN="yyyy-MM-d";

    public static LocalDate getLocalDate(String date) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_PATTERN);

        return LocalDate.parse(date, formatter);


    }
}

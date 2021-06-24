package com.webfactory.exchanger.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexValidator {

    public static final Pattern VALID_DATE_REGEX =
            Pattern.compile("^[0-9]{4}-([0-9]{2}|[1-9]{1})-([0-9]{2}|[1-9]{1})$");

    public static boolean validateDate(String date){

        if(date.isBlank()){
            return false;
        }
        Matcher matcher= VALID_DATE_REGEX.matcher(date);
        return matcher.find();

    }
}

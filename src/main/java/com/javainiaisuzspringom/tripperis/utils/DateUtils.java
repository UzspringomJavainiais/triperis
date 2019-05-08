package com.javainiaisuzspringom.tripperis.utils;


import java.sql.Timestamp;

public class DateUtils {
    public static Timestamp now() {
        return new Timestamp(new java.util.Date().getTime());
    }
}

package com.javainiaisuzspringom.tripperis.utils;


import org.apache.commons.lang3.time.FastDateFormat;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.ZoneId;
import java.util.TimeZone;

public class DateUtils {

    public static final TimeZone HELSINKI_TIME_ZONE = TimeZone.getTimeZone("Europe/Helsinki");
    private static final ZoneId HELSINKI_ZONE_ID = ZoneId.of("Europe/Helsinki");

    public static FastDateFormat formatYMd = FastDateFormat.getInstance("yyyy-MM-dd", HELSINKI_TIME_ZONE);

    public static Timestamp now() {
        return new Timestamp(new java.util.Date().getTime());
    }

    public static Timestamp timestamp(String date) throws ParseException {
        if (date == null || date.isEmpty())
            return null;

        return new Timestamp(formatYMd.parse(date).getTime());
    }
}

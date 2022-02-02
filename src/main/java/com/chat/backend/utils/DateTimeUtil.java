package com.chat.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

@Component
public class DateTimeUtil {

    @Value("${app.dateTimeFormat}")
    private String format;

    @Value("${app.timeZone}")
    private String timeZone;

    public String now(){
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone) );
        return sdf.format(Calendar.getInstance().getTime());
    }
}

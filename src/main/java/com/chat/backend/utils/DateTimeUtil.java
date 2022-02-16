package com.chat.backend.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


@Component
public class DateTimeUtil {

    @Value("${app.dateTimeFormat}")
    private String dateTimeFormat;

    @Value("${app.dateFormat}")
    private String dateFormat;

    @Value("${app.timeZone}")
    private String timeZone;

    public String now(){
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone) );
        return sdf.format(Calendar.getInstance().getTime());
    }

    /**
     * @return value is in milliseconds
     */
    public long difference(String time1, String time2) throws ParseException {
        return Math.abs(
                parse(time1).getTime() - parse(time2).getTime()
        );
    }

    public Date parse(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat(dateTimeFormat, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone) );
        return sdf.parse(time);
    }

    public String getDateOnly(String datetime) {
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone(timeZone) );
        return sdf.format(Calendar.getInstance().getTime());
    }
}

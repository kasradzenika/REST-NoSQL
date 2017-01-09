package com.onenow.hedgefund.nosqlrest.utils;

import com.onenow.hedgefund.logging.Watchr;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateUtil
{
    public static String getCurrentDate()
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        dateFormatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return dateFormatter.format(new Date());
    }

    public static Date getDate(String date, String timeZone, String format)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(timeZone));

        try
        {
            return dateFormatter.parse(date);
        }
        catch (ParseException e)
        {
            Watchr.log(ExceptionUtil.exceptionToString(e));
        }
        return new Date();
    }

    public static String getDate(Date date, String timeZone, String format)
    {
        SimpleDateFormat dateFormatter = new SimpleDateFormat(format);
        dateFormatter.setTimeZone(TimeZone.getTimeZone(timeZone));

        return dateFormatter.format(date);
    }


}

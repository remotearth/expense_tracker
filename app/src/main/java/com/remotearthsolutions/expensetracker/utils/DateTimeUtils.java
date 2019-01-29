package com.remotearthsolutions.expensetracker.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeUtils {

    public static final String dd_MM_yyyy = "dd-MM-yyyy";

    public static String getCurrentDate(String format){
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static String getDate(String format, int days){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH,days);
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        return dateFormat.format(calendar.getTime());
    }

    public static Calendar getCalendarFromDateString(String format, String date){
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            Date dateObj = dateFormat.parse(date);
            calendar.setTime(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar;
    }

    public static long getTimeInMillisFromDateStr(String dateStr,String format){
        DateFormat dateFormat = new SimpleDateFormat(format, Locale.getDefault());
        Calendar calendar = Calendar.getInstance();
        try {
            Date dateObj = dateFormat.parse(dateStr);
            calendar.setTime(dateObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return calendar.getTimeInMillis();
    }

}

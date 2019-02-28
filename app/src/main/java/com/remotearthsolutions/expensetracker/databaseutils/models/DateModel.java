package com.remotearthsolutions.expensetracker.databaseutils.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateModel {
    private long date;

    public String getDate() {
        Date today_date = new Date(date);

        SimpleDateFormat jdf = new SimpleDateFormat("dd-MM-yyyy");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        return String.valueOf(jdf.format(today_date));
    }

    public void setDate(long date) {
        this.date = date;
    }
}

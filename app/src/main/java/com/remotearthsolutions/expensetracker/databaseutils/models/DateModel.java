package com.remotearthsolutions.expensetracker.databaseutils.models;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class DateModel {

    private long datetime;

    public DateModel(long datetime) {
        this.datetime = datetime;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public String getDate() {
        Date today_date = new Date(datetime);

        SimpleDateFormat jdf = new SimpleDateFormat("dd-MM-yyyy");
        jdf.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        return String.valueOf(jdf.format(today_date));
    }

    public void setDate(long datetime) {
        this.datetime = datetime;
    }

}

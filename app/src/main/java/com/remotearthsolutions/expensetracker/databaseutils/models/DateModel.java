package com.remotearthsolutions.expensetracker.databaseutils.models;

public class DateModel {

    private String datetime;

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getDate() {
        return datetime;
    }

    public DateModel(String datetime) {
        this.datetime = datetime;
    }
}

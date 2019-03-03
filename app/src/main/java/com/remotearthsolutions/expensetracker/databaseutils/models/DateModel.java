package com.remotearthsolutions.expensetracker.databaseutils.models;

import androidx.room.Ignore;
import com.intrusoft.sectionedrecyclerview.Section;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class DateModel implements Section<CategoryExpense> {
    @Ignore
    private List<CategoryExpense> childList;
    private long datetime;

    public DateModel(List<CategoryExpense> childList, long datetime) {
        this.childList = childList;
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

    @Override
    public List<CategoryExpense> getChildItems() {
        return childList;
    }
}

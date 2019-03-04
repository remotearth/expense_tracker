package com.remotearthsolutions.expensetracker.databaseutils.models.dtos;

import androidx.room.Ignore;

public class CategoryExpense {

    private int category_id;
    private String category_name;
    private String icon_name;
    private double total_amount;
    private long datetime;

    @Ignore
    public boolean isHeader = false;

    public CategoryExpense(int category_id, String category_name, String icon_name, double total_amount, long datetime) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.icon_name = icon_name;
        this.total_amount = total_amount;
        this.datetime = datetime;
    }

    public CategoryExpense() {}

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getIcon_name() {
        return icon_name;
    }

    public void setIcon_name(String icon_name) {
        this.icon_name = icon_name;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    @Override
    public String toString() {
        return category_id +
                ", " + category_name +
                ", " + total_amount +
                "\n";
    }
}

package com.remotearthsolutions.expensetracker.databaseutils.models.dtos;

import androidx.room.Ignore;

public class CategoryExpense {

    private int category_id;
    private String category_name;
    private String icon_name;
    private double total_amount;
    private long datetime;
    private int account_id;
    @Ignore
    private String account_name;

    @Ignore
    public boolean isHeader = false;

    public CategoryExpense(int category_id, String category_name, String icon_name, double total_amount, long datetime) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.icon_name = icon_name;
        this.total_amount = total_amount;
        this.datetime = datetime;
    }

    public CategoryExpense(int category_id, String category_name, String icon_name, double total_amount, long datetime, int account_id) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.icon_name = icon_name;
        this.total_amount = total_amount;
        this.datetime = datetime;
        this.account_id = account_id;
    }

    public CategoryExpense() {
    }

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

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    @Override
    public String toString() {
        return category_id + ", " +
                category_name + ", " +
                icon_name + ", " +
                total_amount + ", " +
                datetime + "\n";
    }
}

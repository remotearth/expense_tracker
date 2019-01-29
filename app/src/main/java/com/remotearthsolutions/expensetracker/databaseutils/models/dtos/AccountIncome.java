package com.remotearthsolutions.expensetracker.databaseutils.models.dtos;

public class AccountIncome {

    private int account_id;
    private String account_name;
    private String icon_name;
    private double total_amount;

    public AccountIncome(int account_id, String account_name, String icon_name, double total_amount) {
        this.account_id = account_id;
        this.account_name = account_name;
        this.icon_name = icon_name;
        this.total_amount = total_amount;
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
}

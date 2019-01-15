package com.remotearthsolutions.expensetracker.entities;

public class Accounts {

    private int accountImage;
    private String accountName;
    private Double accountAmount;

    public Accounts(int accountImage, String accountName, Double accountAmount) {
        this.accountImage = accountImage;
        this.accountName = accountName;
        this.accountAmount = accountAmount;
    }

    public int getAccountImage() {
        return accountImage;
    }

    public void setAccountImage(int accountImage) {
        this.accountImage = accountImage;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public Double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(Double accountAmount) {
        this.accountAmount = accountAmount;
    }
}

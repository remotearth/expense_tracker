package com.remotearthsolutions.expensetracker.databaseutils.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "account")
public class AccountModel {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "icon")
    private String icon;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "notremovable")
    private int notremovable;

    @Ignore
    public AccountModel() {
    }

    public AccountModel(String name, String icon, double amount, int notremovable) {
        this.name = name;
        this.icon = icon;
        this.amount = amount;
        this.notremovable = notremovable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public int getNotremovable() {
        return notremovable;
    }

    public void setNotremovable(int notremovable) {
        this.notremovable = notremovable;
    }
}

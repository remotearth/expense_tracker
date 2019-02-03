package com.remotearthsolutions.expensetracker.databaseutils.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "expense")
public class ExpenseModel {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "category_id")
    private int categoryId;

    @ColumnInfo(name = "account_id")
    private int source;

    @ColumnInfo(name = "amount")
    private double amount;

    @ColumnInfo(name = "datetime")
    private long datetime;

    public ExpenseModel(int categoryId, int source, double amount, long datetime) {
        this.categoryId = categoryId;
        this.source = source;
        this.amount = amount;
        this.datetime = datetime;
    }

    @Ignore
    public ExpenseModel() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public int getSource() {
        return source;
    }

    public void setSource(int source) {
        this.source = source;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }
}

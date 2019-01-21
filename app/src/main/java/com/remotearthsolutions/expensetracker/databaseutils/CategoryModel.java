package com.remotearthsolutions.expensetracker.databaseutils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class CategoryModel {

    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo(name = "categoryname")
    private int name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }
}

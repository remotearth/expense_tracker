package com.remotearthsolutions.expensetracker.databaseutils;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class CategoryModel {

    @PrimaryKey (autoGenerate = true)
    private int id;

    @ColumnInfo(name = "category_name")
    private String name;


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
}

package com.remotearthsolutions.expensetracker.databaseutils.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import org.parceler.Parcel;

@Parcel
@Entity(tableName = "category")
public class CategoryModel {

    @NonNull
    @PrimaryKey (autoGenerate = true)
    int id;

    @ColumnInfo(name = "category_name")
    String name;

    @ColumnInfo(name = "icon_name")
    String icon;

    @Ignore
    public CategoryModel() {
    }

    public CategoryModel(String name, String icon) {
        this.name = name;
        this.icon = icon;
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
}

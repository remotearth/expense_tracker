package com.remotearthsolutions.expensetracker.databaseutils;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

public interface CategoryDao {

    @Insert
    public void addCategory(CategoryModel categoryModel);

    @Query("Select * from category")
    public List<CategoryModel> getCategory();

    @Delete
    public void deleteCategory(CategoryModel categoryModel);

    @Update
    public void updateCategory(CategoryModel categoryModel);
}

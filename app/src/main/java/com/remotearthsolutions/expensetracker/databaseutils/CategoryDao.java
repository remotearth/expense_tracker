package com.remotearthsolutions.expensetracker.databaseutils;

import androidx.room.*;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert
    void addCategory(CategoryModel categoryModel);

    @Query("Select * from category")
    List<CategoryModel> getCategory();

    @Delete
    void deleteCategory(CategoryModel categoryModel);

    @Update
    void updateCategory(CategoryModel categoryModel);
}

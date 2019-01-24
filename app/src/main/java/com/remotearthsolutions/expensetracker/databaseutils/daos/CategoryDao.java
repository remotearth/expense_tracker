package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCategory(CategoryModel categoryModel);

    @Query("Select * from category")
    Flowable<List<CategoryModel>> getAllCategories();

    @Delete
    void deleteCategory(CategoryModel categoryModel);

    @Update
    void updateCategory(CategoryModel categoryModel);
}

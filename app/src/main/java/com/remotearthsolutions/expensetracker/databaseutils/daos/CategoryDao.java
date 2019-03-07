package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addCategory(CategoryModel categoryModel);

    @Insert
    void addAllCategories(CategoryModel... categoryModels);

    @Query("Select * from category")
    Flowable<List<CategoryModel>> getAllCategories();

    @Delete
    void deleteCategory(CategoryModel categoryModel);

    @Query("DELETE FROM category")
    void deleteAll();

    @Update
    void updateCategory(CategoryModel categoryModel);

    @Query("SELECT COUNT(category_name) FROM category")
    LiveData<Integer> getDataCount();
}

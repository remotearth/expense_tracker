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

    @Query("SELECT ctg.id as category_id, ctg.category_name,ctg.icon_name,exp.account_id, exp.datetime,SUM(exp.amount) as total_amount " +
            "FROM category  as ctg LEFT JOIN expense as exp ON ctg.id = exp.category_id " +
            "AND exp.datetime >= :startTime AND exp.datetime <= :endTime GROUP BY ctg.id ")
    Flowable<List<CategoryExpense>> getAllCategoriesWithExpense(long startTime, long endTime);

    @Delete
    void deleteCategory(CategoryModel categoryModel);

    @Query("DELETE FROM category")
    void deleteAll();

    @Update
    void updateCategory(CategoryModel categoryModel);

    @Query("SELECT COUNT(category_name) FROM category")
    LiveData<Integer> getDataCount();
}

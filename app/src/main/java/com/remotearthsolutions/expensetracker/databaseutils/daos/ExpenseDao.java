package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void add(ExpenseModel expenseModel);

    @Query("Select sum(amount) from expense where category_id = :id")
    int getTotalAmountByCategoryId(int id);

    @Query("SELECT ctg.id as category_id, ctg.category_name,ctg.icon_name,SUM(exp.amount) as total_amount " +
            "FROM category  as ctg LEFT JOIN expense as exp ON ctg.id = exp.category_id " +
            "AND exp.datetime >= :startTime AND exp.datetime <= :endTime GROUP BY ctg.id ")
    Flowable<List<CategoryExpense>> getAllFilterExpense(long startTime, long endTime);

    @Delete
    void deleteExpenseAmount(ExpenseModel accountModel);

    @Update
    void updateExpenseAmount(ExpenseModel accountModel);
}

package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.DateModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void add(ExpenseModel expenseModel);

    @Update
    void updateExpenseAmount(ExpenseModel accountModel);

//    @Query("SELECT DISTINCT exp.datetime " +
//            "FROM category AS ctg " +
//            "LEFT JOIN expense AS exp " +
//            "ON ctg.id = exp.category_id " +
//            "WHERE exp.datetime >= :startTime AND exp.datetime <= :endTime " +
//            "ORDER BY exp.datetime ASC")
//    Flowable<List<DateModel>> getDateWithinRange(long startTime, long endTime);

    @Query("Select sum(amount) from expense where category_id = :id")
    int getTotalAmountByCategoryId(int id);

    @Delete
    void deleteExpenseAmount(ExpenseModel accountModel);

    @Query("DELETE FROM expense")
    void deleteAll();

    @Query("Select * from expense")
    Flowable<List<ExpenseModel>> getAllExpenseEntry();

}

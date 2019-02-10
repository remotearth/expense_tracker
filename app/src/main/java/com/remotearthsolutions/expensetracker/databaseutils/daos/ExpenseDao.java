package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void add(ExpenseModel expenseModel);

    @Query("Select sum(amount) from expense where category_id = :id")
    int getTotalAmountByCategoryId(int id);

    @Query("Select * from expense")
    Flowable<List<ExpenseModel>> getAllExpense();

    @Delete
    void deleteExpenseAmount(ExpenseModel accountModel);

    @Update
    void updateExpenseAmount(ExpenseModel accountModel);
}

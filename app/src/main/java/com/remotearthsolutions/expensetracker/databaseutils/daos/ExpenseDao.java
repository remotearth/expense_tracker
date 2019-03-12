package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface ExpenseDao {
    @Insert
    void add(ExpenseModel expenseModel);

    @Update
    void updateExpenseAmount(ExpenseModel accountModel);

    @Query("Select sum(amount) from expense where category_id = :id")
    int getTotalAmountByCategoryId(int id);

    @Delete
    void deleteExpenseAmount(ExpenseModel accountModel);

    @Query("DELETE FROM expense WHERE id = :expenseId")
    void delete(int expenseId);

    @Query("DELETE FROM expense")
    void deleteAll();

    @Query("Select * from expense")
    Flowable<List<ExpenseModel>> getAllExpenseEntry();

}

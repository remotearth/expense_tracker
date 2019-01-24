package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.IncomeModel;

@Dao
public interface IncomeDao {
    @Insert
    void add(IncomeModel incomeModel);

    @Query("Select sum(amount) from income where account_id = :id")
    int getTotalAmountByAccountId(int id);

    @Delete
    void deleteIncomeAmount(IncomeModel accountModel);

    @Update
    void updateIncomeAmount(IncomeModel accountModel);

}

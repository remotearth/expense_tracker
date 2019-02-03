package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountTransactionModel;

@Dao
public interface AccountTransactionDao {
    @Insert
    void add(AccountTransactionModel accountTransactionModel);

    @Query("Select sum(amount) from account_transaction where account_id = :id")
    int getTotalAmountByAccountId(int id);

    @Delete
    void deleteAccountTransaction(AccountTransactionModel accountModel);

    @Update
    void updateAccountTransaction(AccountTransactionModel accountModel);

}

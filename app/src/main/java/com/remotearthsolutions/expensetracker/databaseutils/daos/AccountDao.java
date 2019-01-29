package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAccount(AccountModel accountModel);

    @Query("Select * from account")
    Flowable<List<AccountModel>> getAllAccounts();

    @Query("SELECT inc.account_id, acc.account_name,acc.icon_name,SUM(inc.amount) as total_amount\n" +
            "FROM account as acc \n" +
            "LEFT JOIN income as inc \n" +
            "ON acc.id = inc.account_id \n" +
            "GROUP BY acc.id")
    Flowable<List<AccountIncome>> getAllAccountsWithAmount();

    @Delete
    void deleteAccount(AccountModel accountModel);

    @Update
    void updateAccount(AccountModel accountModel);
}

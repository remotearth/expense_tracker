package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.util.List;

@Dao
public interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAccount(AccountModel accountModel);

    @Insert
    void addAllAccounts(AccountModel... accountModels);

    @Query("Select * from account")
    Flowable<List<AccountModel>> getAllAccounts();

    @Query("SELECT inc.account_id, acc.account_name,acc.icon_name,SUM(inc.amount) as total_amount\n" +
            "FROM account as acc \n" +
            "LEFT JOIN account_transaction as inc \n" +
            "ON acc.id = inc.account_id \n" +
            "GROUP BY acc.id")
    Flowable<List<AccountIncome>> getAllAccountsWithAmount();

    @Query("SELECT * FROM account WHERE id=:id")
    Single<AccountModel> getAccountById(int id);

    @Query("SELECT inc.account_id, acc.account_name,acc.icon_name,SUM(inc.amount) as total_amount\n" +
            "FROM account as acc \n" +
            "LEFT JOIN account_transaction as inc \n" +
            "WHERE inc.account_id=:id")
    Single<AccountIncome> getAccountWithAmountById(int id);

    @Delete
    void deleteAccount(AccountModel accountModel);

    @Update
    void updateAccount(AccountModel accountModel);
}

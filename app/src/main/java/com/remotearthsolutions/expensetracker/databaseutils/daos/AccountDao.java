package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.AccountIncome;
import io.reactivex.Flowable;
import io.reactivex.Single;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@Dao
public interface AccountDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addAccount(AccountModel accountModel);

    @Insert
    void addAllAccounts(AccountModel... accountModels);

    @Query("Select * from account")
    Flowable<List<AccountModel>> getAllAccounts();

    @Query("SELECT acc.id as account_id, acc.account_name,acc.icon_name,SUM(inc.amount) as total_amount\n" +
            "FROM account as acc \n" +
            "LEFT JOIN account_transaction as inc \n" +
            "ON acc.id = inc.account_id \n" +
            "GROUP BY acc.id")
    Flowable<List<AccountIncome>> getAllAccountsWithAmount();

    @Query("SELECT acc.id as account_id, acc.account_name,acc.icon_name,SUM(inc.amount) as total_amount\n" +
            "FROM account as acc \n" +
            "LEFT JOIN account_transaction as inc \n" +
            "ON acc.id = inc.account_id \n" +
            "GROUP BY acc.id")
    Flowable<ArrayList<AccountIncome>> getAllAccount();


    @Query("SELECT * FROM account WHERE id=:id")
    Single<AccountModel> getAccountById(int id);

    @Query("SELECT acc.id as account_id, acc.account_name,acc.icon_name,SUM(inc.amount) as total_amount "
            + "FROM account as acc LEFT JOIN account_transaction as inc WHERE acc.id=:id")
    Single<AccountIncome> getAccountWithAmountById(int id);

    @Delete
    void deleteAccount(AccountModel accountModel);

    @Update
    void updateAccount(AccountModel accountModel);
}

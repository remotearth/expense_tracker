package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.lifecycle.LiveData;
import androidx.room.*;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
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

    @Query("SELECT * FROM account WHERE id=:id")
    Single<AccountModel> getAccountById(int id);

    @Delete
    void deleteAccount(AccountModel accountModel);

    @Query("DELETE FROM account")
    void deleteAll();

    @Update
    void updateAccount(AccountModel accountModel);

    @Query("SELECT COUNT(name) FROM account")
    LiveData<Integer> countAccount();

}

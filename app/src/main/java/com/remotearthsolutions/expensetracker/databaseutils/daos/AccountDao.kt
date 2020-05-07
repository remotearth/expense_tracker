package com.remotearthsolutions.expensetracker.databaseutils.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface AccountDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addAccount(accountModel: AccountModel)

    @Insert
    fun addAllAccounts(vararg accountModels: AccountModel)

    @get:Query("Select * from account")
    val allAccounts: Single<List<AccountModel>>

    @Query("SELECT * FROM account WHERE id=:id")
    fun getAccountById(id: Int): Single<AccountModel>

    @get:Query("SELECT sum(amount) FROM account")
    val totalAmount: LiveData<Double>

    @Delete
    fun deleteAccount(accountModel: AccountModel)

    @Query("DELETE FROM account")
    fun deleteAll()

    @Update
    fun updateAccount(accountModel: AccountModel)

    @Query("SELECT COUNT(name) FROM account")
    fun countAccount(): LiveData<Int>
}
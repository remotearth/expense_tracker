package com.remotearthsolutions.expensetracker.databaseutils.daos

import androidx.room.*
import com.remotearthsolutions.expensetracker.databaseutils.models.ExpenseModel
import io.reactivex.Flowable
import io.reactivex.Single

@Dao
interface ExpenseDao {
    @Insert
    fun add(expenseModel: ExpenseModel)

    @Update
    fun updateExpenseAmount(accountModel: ExpenseModel)

    @Query("Select sum(amount) from expense where category_id = :id")
    fun getTotalAmountByCategoryId(id: Int): Int

    @Query("Select sum(amount) from expense where datetime>=:startDate AND datetime<=:endDate")
    fun getTotalAmountInDateRange(
        startDate: Long,
        endDate: Long
    ): Single<Double>

    @Delete
    fun deleteExpenseAmount(accountModel: ExpenseModel)

    @Query("DELETE FROM expense WHERE id = :expenseId")
    fun delete(expenseId: Int)

    @Query("DELETE FROM expense")
    fun deleteAll()

    @get:Query("Select * from expense")
    val allExpenseEntry: Flowable<List<ExpenseModel>>
}
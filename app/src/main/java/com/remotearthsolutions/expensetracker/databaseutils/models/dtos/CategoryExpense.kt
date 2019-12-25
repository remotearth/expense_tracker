package com.remotearthsolutions.expensetracker.databaseutils.models.dtos

import androidx.room.ColumnInfo
import androidx.room.Ignore
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils
import org.parceler.Parcel

@Parcel(Parcel.Serialization.BEAN)
class CategoryExpense {
    @ColumnInfo(name = "expense_id")
    var expenseId = 0
    @ColumnInfo(name = "category_id")
    var categoryId = 0
    @ColumnInfo(name = "category_name")
    var categoryName: String? = null
    @ColumnInfo(name = "category_icon")
    var categoryIcon: String? = null
    @ColumnInfo(name = "total_amount")
    var totalAmount = 0.0
    @ColumnInfo(name = "datetime")
    var datetime: Long = 0
    @ColumnInfo(name = "account_id")
    var accountId = 0
    @ColumnInfo(name = "account_name")
    var accountName: String? = null
    @ColumnInfo(name = "account_icon")
    var accountIcon: String? = null
    @ColumnInfo(name = "note")
    var note: String? = null
    @Ignore
    var isHeader = false

    constructor(
        category_id: Int,
        category_name: String?,
        category_icon: String?,
        total_amount: Double,
        datetime: Long
    ) {
        categoryId = category_id
        categoryName = category_name
        categoryIcon = category_icon
        totalAmount = total_amount
        this.datetime = datetime
    }

    constructor(
        expense_id: Int,
        category_id: Int,
        category_name: String?,
        category_icon: String?,
        total_amount: Double,
        datetime: Long,
        account_id: Int,
        account_name: String?,
        account_icon: String?,
        note: String?
    ) {
        expenseId = expense_id
        categoryId = category_id
        categoryName = category_name
        categoryIcon = category_icon
        totalAmount = total_amount
        this.datetime = datetime
        accountId = account_id
        accountName = account_name
        accountIcon = account_icon
        this.note = note
    }

    constructor() {}

    override fun toString(): String {
        return DateTimeUtils.getDate(datetime, DateTimeUtils.dd_MM_yyyy_h_mm).toString() + ", " +
                categoryName + ", " +
                totalAmount + ", " +
                accountName + ", " +
                note + "\n"
    }

    fun setCategory(category: CategoryModel) {
        categoryId = category.id
        categoryName = category.name
        categoryIcon = category.icon
    }

    fun setAccount(account: AccountModel) {
        accountId = account.id
        accountName = account.name
        accountIcon = account.icon
    }

    fun copy() : CategoryExpense{
        val categoryExpense = CategoryExpense()
        categoryExpense.expenseId = this.expenseId
        categoryExpense.categoryId = this.categoryId
        categoryExpense.categoryName = this.categoryName
        categoryExpense.categoryIcon = this.categoryIcon
        categoryExpense.totalAmount = this.totalAmount
        categoryExpense.datetime = this.datetime
        categoryExpense.accountId = this.accountId
        categoryExpense.accountName = this.accountName
        categoryExpense.accountIcon = this.accountIcon
        categoryExpense.note = this.note
        categoryExpense.isHeader = this.isHeader
        return categoryExpense
    }
}
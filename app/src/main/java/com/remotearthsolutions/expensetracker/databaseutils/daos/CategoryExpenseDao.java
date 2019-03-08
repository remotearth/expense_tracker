package com.remotearthsolutions.expensetracker.databaseutils.daos;

import androidx.room.Dao;
import androidx.room.Query;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import io.reactivex.Flowable;

import java.util.List;

@Dao
public interface CategoryExpenseDao {

    @Query("SELECT ctg.id AS category_id, ctg.category_name, ctg.icon_name as category_icon, exp.id as expense_id, exp.account_id, acc.name as account_name, " +
            "acc.icon as account_icon, exp.datetime, exp.amount AS total_amount, exp.note " +
            "FROM category AS ctg " +
            "LEFT JOIN expense as exp " +
            "ON ctg.id = exp.category_id " +
            "INNER JOIN account as acc " +
            "ON exp.account_id = acc.id " +
            "ORDER BY exp.datetime ASC")
    Flowable<List<CategoryExpense>> getAllFilterExpense();

    @Query("SELECT ctg.id AS category_id, ctg.category_name, ctg.icon_name as category_icon, exp.id as expense_id,  exp.account_id, acc.name as account_name, " +
            "acc.icon as account_icon, exp.datetime, exp.amount AS total_amount, exp.note " +
            "FROM category AS ctg " +
            "LEFT JOIN expense AS exp " +
            "ON ctg.id = exp.category_id " +
            "INNER JOIN account as acc " +
            "ON exp.account_id = acc.id " +
            "WHERE exp.datetime >= :startTime AND exp.datetime <= :endTime " +
            "ORDER BY exp.datetime ASC")
    Flowable<List<CategoryExpense>> getExpenseWithinRange(long startTime, long endTime);

    @Query("SELECT ctg.id AS category_id, ctg.category_name, ctg.icon_name as category_icon, exp.id as expense_id,  exp.account_id, acc.name as account_name, " +
            "acc.icon as account_icon, exp.datetime, SUM(exp.amount) AS total_amount, exp.note " +
            "FROM category  as ctg LEFT JOIN expense as exp ON ctg.id = exp.category_id " +
            "AND exp.datetime >= :startTime AND exp.datetime <= :endTime " +
            "INNER JOIN account as acc " +
            "ON exp.account_id = acc.id " +
            "GROUP BY ctg.id ")
    Flowable<List<CategoryExpense>> getAllCategoriesWithExpense(long startTime, long endTime);

}

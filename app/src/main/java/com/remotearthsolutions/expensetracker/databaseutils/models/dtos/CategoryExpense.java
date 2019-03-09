package com.remotearthsolutions.expensetracker.databaseutils.models.dtos;

import androidx.room.Ignore;
import com.remotearthsolutions.expensetracker.databaseutils.models.AccountModel;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;
import org.parceler.Parcel;

@Parcel
public class CategoryExpense {

    private int expense_id;
    private int category_id;
    private String category_name;
    private String category_icon;
    private double total_amount;
    private long datetime;
    private int account_id;
    private String account_name;
    private String account_icon;
    private String note;

    @Ignore
    public boolean isHeader = false;

    public CategoryExpense(int category_id, String category_name, String category_icon, double total_amount, long datetime) {
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_icon = category_icon;
        this.total_amount = total_amount;
        this.datetime = datetime;
    }

    public CategoryExpense(int expense_id, int category_id, String category_name, String category_icon, double total_amount, long datetime,
                           int account_id, String account_name, String account_icon, String note) {
        this.expense_id = expense_id;
        this.category_id = category_id;
        this.category_name = category_name;
        this.category_icon = category_icon;
        this.total_amount = total_amount;
        this.datetime = datetime;
        this.account_id = account_id;
        this.account_name = account_name;
        this.account_icon = account_icon;
        this.note = note;
    }

    public CategoryExpense() {
    }

    public int getExpense_id() {
        return expense_id;
    }

    public void setExpense_id(int expense_id) {
        this.expense_id = expense_id;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_icon() {
        return category_icon;
    }

    public void setCategory_icon(String category_icon) {
        this.category_icon = category_icon;
    }

    public double getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(double total_amount) {
        this.total_amount = total_amount;
    }

    public long getDatetime() {
        return datetime;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }

    public int getAccount_id() {
        return account_id;
    }

    public void setAccount_id(int account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_icon() {
        return account_icon;
    }

    public void setAccount_icon(String account_icon) {
        this.account_icon = account_icon;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return DateTimeUtils.getDate(datetime, DateTimeUtils.dd_MM_yyyy) + ", " +
                category_name + ", " +
                total_amount + ", " +
                account_name + ", " +
                note + "\n";
    }

    public void setCategory(CategoryModel category) {
        this.category_id = category.getId();
        this.category_name = category.getName();
        this.category_icon = category.getIcon();
    }

    public void setAccount(AccountModel account) {
        this.account_id = account.getId();
        this.account_name = account.getName();
        this.account_icon = account.getIcon();
    }
}

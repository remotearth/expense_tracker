package com.remotearthsolutions.expensetracker.viewmodels;

import androidx.lifecycle.ViewModel;
import com.remotearthsolutions.expensetracker.R;
import com.remotearthsolutions.expensetracker.contracts.ExpenseFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.daos.ExpenseDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.utils.DateTimeUtils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class ExpenseViewModel extends ViewModel {

    private ExpenseFragmentContract.ExpenseView view;
    private CategoryExpenseDao categoryExpenseDao;
    private ExpenseDao expenseDao;
    private CompositeDisposable disposable = new CompositeDisposable();
    private int dateRangeBtnId;

    public ExpenseViewModel(ExpenseFragmentContract.ExpenseView view, ExpenseDao expenseDao, CategoryExpenseDao categoryExpenseDao) {
        this.view = view;
        this.expenseDao = expenseDao;
        this.categoryExpenseDao = categoryExpenseDao;
    }

    public void loadFilterExpense(long startTime, long endTime, int btnId) {
        disposable.add(categoryExpenseDao.getExpenseWithinRange(startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((listOfFilterExpense) -> {

                    List<CategoryExpense> expenseList = new ArrayList<>();

                    if (listOfFilterExpense.size() > 0) {
                        long previousDate = listOfFilterExpense.get(0).getDatetime();
                        String previousMonth = DateTimeUtils.getDate(previousDate, DateTimeUtils.mmmm);

                        if (btnId != R.id.nextDateBtn && btnId != R.id.previousDateBtn) {
                            dateRangeBtnId = btnId;
                        }

                        if (dateRangeBtnId == R.id.yearlyRangeBtn) {
                            CategoryExpense monthHeader = new CategoryExpense();
                            monthHeader.isHeader = true;
                            monthHeader.setCategory_name(previousMonth);
                            expenseList.add(monthHeader);
                        }

                        if (dateRangeBtnId != R.id.dailyRangeBtn) {
                            CategoryExpense header = new CategoryExpense();
                            header.isHeader = true;
                            header.setCategory_name(DateTimeUtils.getDate(previousDate, DateTimeUtils.dd_MM_yyyy));
                            expenseList.add(header);
                        }

                        for (int i = 0; i < listOfFilterExpense.size(); i++) {
                            CategoryExpense expense = listOfFilterExpense.get(i);

                            if (dateRangeBtnId == R.id.yearlyRangeBtn) {
                                String monthName = DateTimeUtils.getDate(expense.getDatetime(), DateTimeUtils.mmmm);
                                if (!monthName.equals(previousMonth)) {
                                    CategoryExpense monthHeader = new CategoryExpense();
                                    monthHeader.isHeader = true;
                                    monthHeader.setCategory_name(monthName);
                                    expenseList.add(monthHeader);
                                    previousMonth = monthName;
                                }
                            }

                            if (dateRangeBtnId != R.id.dailyRangeBtn) {
                                if (!DateTimeUtils.getDate(expense.getDatetime(), DateTimeUtils.dd_MM_yyyy)
                                        .equals(DateTimeUtils.getDate(previousDate, DateTimeUtils.dd_MM_yyyy))) {
                                    CategoryExpense dummy = new CategoryExpense();
                                    dummy.isHeader = true;
                                    dummy.setCategory_name(DateTimeUtils.getDate(expense.getDatetime(), DateTimeUtils.dd_MM_yyyy));
                                    previousDate = expense.getDatetime();
                                    expenseList.add(dummy);
                                }
                            }

                            expenseList.add(expense);
                        }
                    }

                    view.loadFilterExpense(expenseList);

                }));
    }
}





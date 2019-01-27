package com.remotearthsolutions.expensetracker.viewmodels;

import com.remotearthsolutions.expensetracker.contracts.HomeFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense;
import com.remotearthsolutions.expensetracker.entities.ExpeneChartData;
import com.remotearthsolutions.expensetracker.utils.Utils;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

public class HomeFragmentViewModel {

    private HomeFragmentContract.View view;
    private CategoryDao categoryDao;
    private CompositeDisposable disposable = new CompositeDisposable();

    public HomeFragmentViewModel(HomeFragmentContract.View view, CategoryDao categoryDao) {
        this.view = view;
        this.categoryDao = categoryDao;
    }

    public void init() {

        disposable.add(categoryDao.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((categories) -> view.showCategories(categories)));

    }

    public void loadExpenseChart() {

        disposable.add(categoryDao.getAllCategoriesWithExpense()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe((listOfCategoryWithAmount) -> {

                    double sum = 0;
                    for (CategoryExpense expense : listOfCategoryWithAmount) {
                        sum += expense.getTotal_amount();
                    }

                    List<ExpeneChartData> chartDataList = new ArrayList<>();
                    for (CategoryExpense expense : listOfCategoryWithAmount) {
                        double val = (expense.getTotal_amount() / sum) * 100;
                        ExpeneChartData data = new ExpeneChartData(val, Utils.getRandomColorHexValue(), expense.getCategory_name());
                        chartDataList.add(data);
                    }

                    view.loadExpenseChart(chartDataList);


                }));

    }
}

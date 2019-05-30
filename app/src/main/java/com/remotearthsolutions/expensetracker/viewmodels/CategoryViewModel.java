package com.remotearthsolutions.expensetracker.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.remotearthsolutions.expensetracker.contracts.CategoryFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import java.util.List;

public class CategoryViewModel extends ViewModel {

    private CategoryFragmentContract.View view;
    private CategoryDao categoryDao;
    private CompositeDisposable disposable = new CompositeDisposable();


    public CategoryViewModel(CategoryFragmentContract.View view, CategoryDao categoryDao) {
        this.view = view;
        this.categoryDao = categoryDao;
    }

    public void showCategories() {
        disposable.add(categoryDao.getAllCategories()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        categories -> view.showCategories(categories)
                ));
    }

    public void updateCategory(CategoryModel categoryModel) {

        disposable.add(Completable.fromAction(() -> categoryDao.updateCategory(categoryModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> showCategories()));

    }

    public void deleteCategory(CategoryModel categoryModel) {
        disposable.add(Completable.fromAction(() -> categoryDao.deleteCategory(categoryModel))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> showCategories()));
    }

    public LiveData<Integer> getNumberOfItem() {
        return categoryDao.getDataCount();
    }
}

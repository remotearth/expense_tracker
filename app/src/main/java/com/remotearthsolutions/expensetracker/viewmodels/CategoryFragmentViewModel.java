package com.remotearthsolutions.expensetracker.viewmodels;

import com.remotearthsolutions.expensetracker.contracts.CategoryFragmentContract;
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao;
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class CategoryFragmentViewModel {

    private CategoryFragmentContract.View view;
    private CategoryDao categoryDao;
    private CompositeDisposable disposable = new CompositeDisposable();


    public CategoryFragmentViewModel(CategoryFragmentContract.View view, CategoryDao categoryDao) {
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
}

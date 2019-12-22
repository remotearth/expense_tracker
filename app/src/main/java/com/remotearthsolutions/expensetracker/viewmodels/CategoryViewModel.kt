package com.remotearthsolutions.expensetracker.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.contracts.CategoryFragmentContract
import com.remotearthsolutions.expensetracker.databaseutils.daos.CategoryDao
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class CategoryViewModel(
    private val view: CategoryFragmentContract.View,
    private val categoryDao: CategoryDao
) : ViewModel() {
    private val disposable = CompositeDisposable()
    fun showCategories() {
        disposable.add(
            categoryDao.allCategories
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { categories: List<CategoryModel> ->
                    view.showCategories(
                        categories
                    )
                }
        )
    }

    fun updateCategory(categoryModel: CategoryModel) {
        disposable.add(Completable.fromAction {
            categoryDao.updateCategory(
                categoryModel
            )
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { showCategories() }
        )
    }

    fun deleteCategory(categoryModel: CategoryModel) {
        disposable.add(Completable.fromAction {
            categoryDao.deleteCategory(categoryModel)
        }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { showCategories() }
        )
    }

    val numberOfItem: LiveData<Int>
        get() = categoryDao.dataCount

}
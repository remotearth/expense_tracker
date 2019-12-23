package com.remotearthsolutions.expensetracker.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.adapters.CategoryListViewAdapter
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.contracts.CategoryFragmentContract
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.fragments.OptionBottomSheetFragment.OptionsFor
import com.remotearthsolutions.expensetracker.viewmodels.CategoryViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_category.view.*

class CategoryFragment : BaseFragment(),
    CategoryFragmentContract.View,
    OptionBottomSheetFragment.Callback {
    private lateinit var mView: View
    private lateinit var adapter: CategoryListViewAdapter
    private var viewModel: CategoryViewModel? = null
    private var selectedCategory: CategoryModel? = null
    private var limitOfCategory = 0
    private var mContext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mView = inflater.inflate(R.layout.fragment_category, container, false)
        mView.cat_recycler.setHasFixedSize(true)
        mView.cat_recycler.layoutManager = LinearLayoutManager(mContext)
        val categoryDao =
            DatabaseClient.getInstance(mContext!!)?.appDatabase?.categoryDao()

        viewModel =
            ViewModelProviders.of(this, BaseViewModelFactory {
                CategoryViewModel(this, categoryDao!!)
            }).get(CategoryViewModel::class.java)

        viewModel!!.showCategories()
        viewModel!!.numberOfItem.observe(
            viewLifecycleOwner,
            Observer { count: Int -> limitOfCategory = count }
        )

        mView.addcategory.setOnClickListener {
            if (limitOfCategory < 20 ||
                ((mContext as Activity?)!!.application as ApplicationObject).isPremium
            ) {
                selectedCategory = null
                onClickEditBtn()
            } else {
                showAlert(
                    getString(R.string.attention),
                    getString(R.string.you_need_to_be_premium_user_to_add_more_categories),
                    getString(R.string.ok),
                    null,
                    null
                )
            }
        }
        return mView
    }

    override fun showCategories(categories: List<CategoryModel>?) {
        adapter = CategoryListViewAdapter(categories)
        adapter.setOnItemClickListener(object : CategoryListViewAdapter.OnItemClickListener {
            override fun onItemClick(categoryModel: CategoryModel?) {
                selectedCategory = categoryModel
                val optionBottomSheetFragment =
                    OptionBottomSheetFragment()
                optionBottomSheetFragment.setCallback(this@CategoryFragment, OptionsFor.CATEGORY)
                optionBottomSheetFragment.show(
                    childFragmentManager,
                    OptionBottomSheetFragment::class.java.name
                )
            }
        })
        mView.cat_recycler.adapter = adapter
    }

    override fun onClickAddAmountBtn() { //THis is not need for category. need to refactor this somehow so this method will not be needed to implement here.
    }

    override fun onClickEditBtn() {
        val fm = childFragmentManager
        val categoryDialogFragment: AddCategoryDialogFragment =
            AddCategoryDialogFragment.newInstance(getString(R.string.update_category))
        categoryDialogFragment.setCategory(selectedCategory)
        categoryDialogFragment.setCallback(object : AddCategoryDialogFragment.Callback {
            override fun onCategoryAdded(categoryModel: CategoryModel?) {
                //viewModel.showCategories();
                categoryDialogFragment.dismiss()
            }
        })
        categoryDialogFragment.show(fm, AddCategoryDialogFragment::class.java.name)
    }

    override fun onClickDeleteBtn() {
        if (selectedCategory!!.notremovable == 1) {
            showToast(getString(R.string.you_cannot_delete_this_category))
            return
        }
        showAlert(
            getString(R.string.warning),
            getString(R.string.deleting_this_category_will_remove_expenses_related_to_this_also_are_you_sure_you_want_to_delete),
            getString(R.string.yes),
            getString(R.string.not_now),
            object : BaseView.Callback {
                override fun onOkBtnPressed() {
                    viewModel!!.deleteCategory(selectedCategory!!)
                    Toast.makeText(
                        activity,
                        getString(R.string.category_deleted_successfully),
                        Toast.LENGTH_LONG
                    ).show()
                }

                override fun onCancelBtnPressed() {}
            })
    }
}
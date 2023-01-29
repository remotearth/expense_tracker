package com.remotearthsolutions.expensetracker.fragments

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.activities.ApplicationObject
import com.remotearthsolutions.expensetracker.activities.main.MainActivity
import com.remotearthsolutions.expensetracker.adapters.CategoryListViewAdapter
import com.remotearthsolutions.expensetracker.contracts.BaseView
import com.remotearthsolutions.expensetracker.contracts.CategoryFragmentContract
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databinding.FragmentCategoryBinding
import com.remotearthsolutions.expensetracker.fragments.OptionBottomSheetFragment.OptionsFor
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.viewmodels.CategoryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CategoryFragment : BaseFragment(),
    CategoryFragmentContract.View,
    OptionBottomSheetFragment.Callback {
    private lateinit var binding: FragmentCategoryBinding
    private lateinit var adapter: CategoryListViewAdapter
    private val viewModel: CategoryViewModel by viewModel { parametersOf(this) }
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
    ): View {
        binding = FragmentCategoryBinding.inflate(layoutInflater, container, false)
        binding.catRecycler.setHasFixedSize(true)
        binding.catRecycler.layoutManager = LinearLayoutManager(mContext)

        viewModel.showCategories()
        viewModel.numberOfItem.observe(
            viewLifecycleOwner
        ) { count: Int -> limitOfCategory = count }

        binding.addcategory.setOnClickListener {
            if (limitOfCategory < 20 ||
                ((mContext as Activity?)!!.application as ApplicationObject).isPremium
            ) {
                selectedCategory = null
                onClickEditBtn()
            } else {
                showAlert(
                    getResourceString(R.string.attention),
                    getResourceString(R.string.you_need_to_be_premium_user_to_add_more_categories),
                    getResourceString(R.string.ok),
                    null,
                    null, null
                )
            }
        }
        registerBackButton()
        return binding.root
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
        binding.catRecycler.adapter = adapter
    }

    override fun onClickEditBtn() {
        val fm = childFragmentManager
        val categoryDialogFragment: AddCategoryDialogFragment =
            AddCategoryDialogFragment.newInstance(getResourceString(R.string.update_category))
        categoryDialogFragment.setCategory(selectedCategory)
        categoryDialogFragment.setCallback(object : AddCategoryDialogFragment.Callback {
            override fun onCategoryAdded(categoryModel: CategoryModel?) {
                viewModel.showCategories()
                categoryDialogFragment.dismiss()
                (activity as MainActivity).onUpdateCategory()
            }
        })
        categoryDialogFragment.show(fm, AddCategoryDialogFragment::class.java.name)
    }

    override fun onClickDeleteBtn() {
        if (selectedCategory!!.notremovable == 1) {
            showToast(getResourceString(R.string.you_cannot_delete_this_category))
            return
        }
        showAlert(
            getResourceString(R.string.warning),
            getResourceString(R.string.deleting_this_category_will_remove_expenses_related_to_this_also_are_you_sure_you_want_to_delete),
            getResourceString(R.string.yes),
            getResourceString(R.string.not_now),
            null,
            object : BaseView.Callback {
                override fun onOkBtnPressed() {
                    viewModel.deleteCategory(selectedCategory!!)
                    Toast.makeText(
                        activity,
                        getResourceString(R.string.category_deleted_successfully),
                        Toast.LENGTH_LONG
                    ).show()
                    (activity as MainActivity).onUpdateCategory()
                    with(AnalyticsManager) {
                        logEvent(EXPENSE_CATEGORY_DELETED)
                    }
                }

                override fun onCancelBtnPressed() {}
            })
    }
}

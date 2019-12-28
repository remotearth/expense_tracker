package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.CategoryListAdapter
import com.remotearthsolutions.expensetracker.contracts.CategoryFragmentContract
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.Utils.getDeviceScreenSize
import com.remotearthsolutions.expensetracker.viewmodels.CategoryViewModel
import com.remotearthsolutions.expensetracker.viewmodels.viewmodel_factory.BaseViewModelFactory
import kotlinx.android.synthetic.main.fragment_add_category.view.*
import java.util.*

class CategoryDialogFragment : DialogFragment(),
    CategoryFragmentContract.View {
    private lateinit var mView: View
    private var viewModel: CategoryViewModel? = null
    private lateinit var layoutManager: GridLayoutManager
    private lateinit var categoryListAdapter: CategoryListAdapter
    private var callback: Callback? =
        null
    private var selectedCategoryId = 0
    private var mContext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun setCallback(callback: Callback?) {
        this.callback = callback
    }

    fun setCategory(categoryId: Int) {
        selectedCategoryId = categoryId
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_category, container)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        mView = view
        val categoryDao =
            DatabaseClient.getInstance(mContext!!)?.appDatabase?.categoryDao()
        val expenseDao =
            DatabaseClient.getInstance(mContext!!)?.appDatabase?.expenseDao()
        val accountDao =
            DatabaseClient.getInstance(mContext!!)?.appDatabase?.accountDao()

        viewModel =
            ViewModelProviders.of(this, BaseViewModelFactory {
                CategoryViewModel(this, categoryDao!!,expenseDao!!, accountDao!!)
            }).get(CategoryViewModel::class.java)
        
        mView.categoryrecyclearView.setHasFixedSize(true)
        layoutManager = GridLayoutManager(mContext, NUMBER_OF_ELEMENT_IN_ROW)
        mView.categoryrecyclearView.layoutManager = layoutManager
        categoryListAdapter = CategoryListAdapter(ArrayList())
        mView.categoryrecyclearView.adapter = categoryListAdapter
        viewModel!!.showCategories()
    }

    override fun showCategories(categories: List<CategoryModel>?) {
        categoryListAdapter = CategoryListAdapter(categories, selectedCategoryId)
        categoryListAdapter.setScreenSize(
            getDeviceScreenSize(mContext)
        )
        categoryListAdapter.setOnItemClickListener(object :
            CategoryListAdapter.OnItemClickListener {
            override fun onItemClick(category: CategoryModel?) {
                callback!!.onSelectCategory(
                    category
                )
            }
        })

        mView.categoryrecyclearView.adapter = categoryListAdapter
        layoutManager.scrollToPosition(getPositionOfSelectedItem(categories, selectedCategoryId))
    }

    private fun getPositionOfSelectedItem(
        categories: List<CategoryModel>?,
        categoryId: Int
    ): Int {
        for (category in categories!!) {
            if (category.id == categoryId) {
                return categories.indexOf(category)
            }
        }
        return 0
    }

    interface Callback {
        fun onSelectCategory(category: CategoryModel?)
    }

    companion object {
        private const val NUMBER_OF_ELEMENT_IN_ROW = 3
        fun newInstance(title: String?): CategoryDialogFragment {
            val frag = CategoryDialogFragment()
            val args = Bundle()
            args.putString(Constants.KEY_TITLE, title)
            frag.arguments = args
            return frag
        }
    }
}
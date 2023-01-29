package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.IconListAdapter
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.databinding.FragmentAddUpdateCategoryAccountBinding
import com.remotearthsolutions.expensetracker.utils.AnalyticsManager
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.allIcons
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.Utils.getDeviceScreenSize
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddCategoryDialogFragment : DialogFragment() {
    private lateinit var binding: FragmentAddUpdateCategoryAccountBinding
    private lateinit var iconListAdapter: IconListAdapter
    private var callback: Callback? = null
    private var categoryModel: CategoryModel? = null
    private var selectedIcon: String? = null
    private lateinit var mContext: Context
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    fun setCallback(callback: Callback?) {
        this.callback = callback
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddUpdateCategoryAccountBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        if (categoryModel != null) {
            binding.nameEdtxt.setText(categoryModel!!.name)
            binding.nameEdtxt.setSelection(binding.nameEdtxt.text.length)
            binding.header.text = requireContext().getString(R.string.update_category)
            binding.okBtn.text = requireContext().getString(R.string.update)
        }
        binding.okBtn.setOnClickListener { saveCategory() }
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getDeviceScreenSize(mContext)!!.height / 2
        )
        binding.accountrecyclearView.layoutParams = params
        binding.accountrecyclearView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(mContext, 4)
        binding.accountrecyclearView.layoutManager = gridLayoutManager
        val allIconList = allIcons
        iconListAdapter = IconListAdapter(allIconList, gridLayoutManager)
        iconListAdapter.setSelectedIcon(if (selectedIcon != null) selectedIcon else "")
        iconListAdapter.setOnItemClickListener(object : IconListAdapter.OnItemClickListener {
            override fun onItemClick(icon: String?) {
                selectedIcon = icon
                iconListAdapter.setSelectedIcon(if (selectedIcon != null) selectedIcon else "")
                iconListAdapter.notifyDataSetChanged()
            }

        })
        binding.accountrecyclearView.adapter = iconListAdapter
    }

    private fun saveCategory() {
        val categoryName = binding.nameEdtxt.text.toString().trim { it <= ' ' }
        if (categoryName.isEmpty()) {
            binding.nameEdtxt.error = requireContext().getString(R.string.enter_a_name)
            binding.nameEdtxt.requestFocus()
            return
        }
        if (categoryName.length > 20) {
            binding.nameEdtxt.error =
                requireContext().getString(R.string.name_should_be_less_than_20_char)
            binding.nameEdtxt.requestFocus()
            return
        }
        if (selectedIcon == null || selectedIcon!!.isEmpty()) {
            Toast.makeText(
                activity,
                requireContext().getString(R.string.select_an_icon),
                Toast.LENGTH_SHORT
            )
                .show()
            return
        }
        val categoryDao =
            DatabaseClient.getInstance(mContext).appDatabase.categoryDao()
        if (categoryModel == null) {
            categoryModel = CategoryModel()
        }
        categoryModel!!.name = categoryName
        categoryModel!!.icon = selectedIcon
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(Completable.fromAction {
            if (categoryModel!!.id > 0) {
                categoryDao.updateCategory(categoryModel)
                with(AnalyticsManager) {
                    logEvent(EXPENSE_CATEGORY_UPDATED)
                }
            } else {
                categoryDao.addCategory(categoryModel!!)
                with(AnalyticsManager) {
                    logEvent(EXPENSE_CATEGORY_CREATED)
                }
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { callback!!.onCategoryAdded(categoryModel) }
        )
    }

    fun setCategory(categoryModel: CategoryModel?) {
        this.categoryModel = categoryModel
        if (categoryModel != null) {
            selectedIcon = categoryModel.icon
        }
    }

    interface Callback {
        fun onCategoryAdded(categoryModel: CategoryModel?)
    }

    companion object {
        fun newInstance(title: String?): AddCategoryDialogFragment {
            val frag = AddCategoryDialogFragment()
            val args = Bundle()
            args.putString(Constants.KEY_TITLE, title)
            frag.arguments = args
            return frag
        }
    }
}
package com.remotearthsolutions.expensetracker.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.adapters.IconListAdapter
import com.remotearthsolutions.expensetracker.databaseutils.DatabaseClient
import com.remotearthsolutions.expensetracker.databaseutils.models.CategoryModel
import com.remotearthsolutions.expensetracker.utils.CategoryIcons.allIcons
import com.remotearthsolutions.expensetracker.utils.Constants
import com.remotearthsolutions.expensetracker.utils.Utils.getDeviceScreenSize
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class AddCategoryDialogFragment : DialogFragment() {
    private lateinit var iconListAdapter: IconListAdapter
    private lateinit var recyclerView: RecyclerView
    private var callback: Callback? =
        null
    private lateinit var categoryNameEdtxt: EditText
    private lateinit var categorydialogstatus: TextView
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
    ): View? {
        return inflater.inflate(R.layout.fragment_add_update_category_account, container)
    }

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(view, savedInstanceState)
        categoryNameEdtxt = view.findViewById(R.id.nameEdtxt)
        categorydialogstatus = view.findViewById(R.id.header)
        val okBtn = view.findViewById<Button>(R.id.okBtn)
        if (categoryModel != null) {
            categoryNameEdtxt.setText(categoryModel!!.name)
            categoryNameEdtxt.setSelection(categoryNameEdtxt.text.length)
            categorydialogstatus.text = getString(R.string.update_category)
            okBtn.text = getString(R.string.update)
        }
        okBtn.setOnClickListener { saveCategory() }
        recyclerView = view.findViewById(R.id.accountrecyclearView)
        val params = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            getDeviceScreenSize(mContext)!!.height / 2
        )
        recyclerView.layoutParams = params
        recyclerView.setHasFixedSize(true)
        val gridLayoutManager = GridLayoutManager(mContext, 4)
        recyclerView.layoutManager = gridLayoutManager
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
        recyclerView.adapter = iconListAdapter
    }

    private fun saveCategory() {
        val categoryName = categoryNameEdtxt.text.toString().trim { it <= ' ' }
        if (categoryName.isEmpty()) {
            categoryNameEdtxt.error = getString(R.string.enter_a_name)
            categoryNameEdtxt.requestFocus()
            return
        }
        if (selectedIcon == null || selectedIcon!!.isEmpty()) {
            Toast.makeText(activity, getString(R.string.select_an_icon), Toast.LENGTH_SHORT)
                .show()
            return
        }
        val categoryDao =
            DatabaseClient.getInstance(mContext)?.appDatabase?.categoryDao()
        if (categoryModel == null) {
            categoryModel = CategoryModel()
        }
        categoryModel!!.name = categoryName
        categoryModel!!.icon = selectedIcon
        val compositeDisposable = CompositeDisposable()
        compositeDisposable.add(Completable.fromAction {
            if (categoryModel!!.id > 0) {
                categoryDao?.updateCategory(categoryModel)
            } else {
                categoryDao?.addCategory(categoryModel!!)
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
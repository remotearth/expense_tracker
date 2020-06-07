package com.remotearthsolutions.expensetracker.fragments.currenypicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.utils.CountryFlagIcons


class CurrencyListAdapter(
    context: Context,
    private val selectedCurrencyName: String,
    private val onClickOnItem: (currencyName: String) -> Unit
) :
    RecyclerView.Adapter<CurrencyViewHolder>(), Filterable {
    private val currencyArray = context.resources.getStringArray(R.array.currency)
    private var currencies = currencyArray.toMutableList()
    private var allCurrencies = currencyArray.toMutableList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrencyViewHolder {
        val v =
            LayoutInflater.from(parent.context).inflate(R.layout.view_currency, parent, false)
        return CurrencyViewHolder(v, currencyArray)
    }

    override fun getItemCount(): Int {
        return currencies.size
    }

    override fun onBindViewHolder(holder: CurrencyViewHolder, position: Int) {
        val currency = currencies[position]
        holder.bind(currency!!, selectedCurrencyName, onClickOnItem)
    }

    override fun getFilter(): Filter {
        return filter
    }

    private val filter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            var filteredList = ArrayList<String>()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(allCurrencies)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim()
                for (item in allCurrencies) {
                    if (item.toLowerCase().contains(filterPattern)) {
                        filteredList.add(item)
                    }
                }
            }

            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            currencies.clear()
            if (results?.values != null) {
                currencies.addAll(results.values as ArrayList<String>)
            }
            notifyDataSetChanged()
        }

    }
}

class CurrencyViewHolder(view: View, private val currencyArray: Array<String>) :
    RecyclerView.ViewHolder(view) {
    private val container: ConstraintLayout = view.findViewById(R.id.container)
    private val flagImage: ImageView = view.findViewById(R.id.imageIv)
    private val currencyName: TextView = view.findViewById(R.id.nameTv)

    fun bind(
        currency: String,
        selectedCurrencyName: String,
        onClickOnItem: (currencyName: String) -> Unit
    ) {
        currencyName.text = currency
        val position = currencyArray.indexOf(currency)
        flagImage.setImageResource(CountryFlagIcons.getIcon(position))
        if (currency == selectedCurrencyName) {
            container.setBackgroundResource(R.color.Ashcolor)
        } else {
            container.setBackgroundResource(0)
        }
        container.setOnClickListener {
            onClickOnItem.invoke(currency)
        }
    }

}
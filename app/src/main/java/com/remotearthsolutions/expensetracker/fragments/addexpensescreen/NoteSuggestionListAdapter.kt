package com.remotearthsolutions.expensetracker.fragments.addexpensescreen

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.viewmodels.NotesViewModel

class NoteSuggestionListAdapter(
    context: Context,
    resource: Int,
    var items: ArrayList<String>,
    private val notesViewModel: NotesViewModel) :
    ArrayAdapter<String>(context, resource, items) {


    override fun getItem(position: Int): String {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = convertView ?: layoutInflater.inflate(R.layout.listitem_notes, parent, false)
        val textView = view.findViewById<TextView>(R.id.noteText)
        val deleteBtn = view.findViewById<ImageView>(R.id.deleteBtn)

        textView.text = items[position]

        deleteBtn.setOnClickListener {
            notesViewModel.deleteNote(items[position])
            items.remove(items[position])
            notifyDataSetChanged()
        }

        return view
    }
}

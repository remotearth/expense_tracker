package com.remotearthsolutions.expensetracker.fragments.addexpensescreen

import android.text.Editable
import android.text.TextWatcher

class NoteEditTexWatcher(
    private val adapter: NoteSuggestionListAdapter,
    private val notes: ArrayList<String>
) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
    }

    override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
    }

    override fun afterTextChanged(s: Editable?) {
        val ss = s.toString().lowercase()
        if (ss.length >= 3) {
            val filteredNotes = ArrayList<String>()
            var n = 0
            for (note in notes) {
                if (note.lowercase().startsWith(ss)) {
                    filteredNotes.add(note)
                    n++
                    if (n == 10) {
                        break
                    }
                }
            }
            adapter.items = filteredNotes
            adapter.notifyDataSetChanged()
        }
    }
}

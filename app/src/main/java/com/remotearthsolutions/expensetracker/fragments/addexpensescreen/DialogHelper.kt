package com.remotearthsolutions.expensetracker.fragments.addexpensescreen

import android.content.Context
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.remotearthsolutions.expensetracker.R
import com.remotearthsolutions.expensetracker.databaseutils.models.NoteModel
import com.remotearthsolutions.expensetracker.databaseutils.models.dtos.CategoryExpense
import com.remotearthsolutions.expensetracker.databinding.FragmentAddExpenseBinding
import com.remotearthsolutions.expensetracker.viewmodels.NotesViewModel


object DialogHelper {

    fun getInputFor(textView: TextView, mContext: Context, layoutInflater: LayoutInflater) {
        val builder = AlertDialog.Builder(mContext).create()
        val dialogView = layoutInflater.inflate(R.layout.view_add_note, null)
        val headerTitle = dialogView.findViewById<TextView>(R.id.header)
        val noteEdtxt = dialogView.findViewById<EditText>(R.id.noteEdtxt)

        headerTitle.text = "ENTER VALUE"
        noteEdtxt.hint = ""
        noteEdtxt.inputType = InputType.TYPE_CLASS_NUMBER
        noteEdtxt.setText(textView.text)
        noteEdtxt.setSelection(textView.text.length)

        dialogView.findViewById<View>(R.id.okBtn)
            .setOnClickListener {
                val number = noteEdtxt.text.toString()
                if (number.isEmpty() || Integer.parseInt(number) <= 0) {
                    Toast.makeText(
                        mContext,
                        "You have to enter a value greater than 0",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    textView.text = noteEdtxt.text.toString()
                    builder.dismiss()
                }
            }
        builder.setView(dialogView)
        builder.show()
    }

    fun showExpenseNoteInput(
        mContext: Context,
        notes: List<String>,
        layoutInflater: LayoutInflater,
        binding: FragmentAddExpenseBinding,
        categoryExpense: CategoryExpense?
    ) {
        val builder =
            AlertDialog.Builder(mContext).create()
        val dialogView =
            layoutInflater.inflate(R.layout.view_add_note, null)
        val headerTitle = dialogView.findViewById<TextView>(R.id.header)
        headerTitle.setText(R.string.expense_note)

        val noteEdtxt = dialogView.findViewById<AutoCompleteTextView>(R.id.noteEdtxt)
        val adapter = ArrayAdapter(mContext, android.R.layout.simple_list_item_1, notes)
        noteEdtxt.setAdapter(adapter)
        noteEdtxt.threshold = 3

        val note = categoryExpense!!.note
        if (note != null) {
            noteEdtxt.setText(categoryExpense.note)
            noteEdtxt.setSelection(categoryExpense.note!!.length)
        }
        dialogView.findViewById<View>(R.id.okBtn)
            .setOnClickListener {
                val str = noteEdtxt.text.toString()
                categoryExpense.note = str
                binding.expenseNoteEdtxt.setText(Helpers.getTruncatedNote(str))
                builder.dismiss()
            }
        dialogView.findViewById<ImageView>(R.id.clearBtn)
            .setOnClickListener {
                noteEdtxt.setText("")
            }
        builder.setView(dialogView)
        builder.show()
    }
}
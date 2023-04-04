package com.remotearthsolutions.expensetracker.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.remotearthsolutions.expensetracker.databaseutils.daos.NotesDao
import com.remotearthsolutions.expensetracker.databaseutils.models.NoteModel
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NotesViewModel(
    private val notesDao: NotesDao,
) : ViewModel() {
    val notesLiveData = MutableLiveData<ArrayList<String>>()
    private val compositeDisposable = CompositeDisposable()

    fun addNote(noteModel: NoteModel) {

        compositeDisposable.add(
            Completable.fromAction {
                notesDao.add(noteModel)
            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
        )
    }

    fun deleteNote(noteModel: NoteModel) {
        compositeDisposable.add(Completable.fromAction {
            notesDao.delete(noteModel)
        }.subscribeOn(
            Schedulers.io()
        )
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {

            }
        )
    }

    fun getAllNotes() {
        compositeDisposable.add(
            notesDao.allNotes
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe { notes: List<NoteModel> ->
                    val listOfNotes = ArrayList<String>()
                    notes.forEach {
                        listOfNotes.add(it.note!!)
                    }
                    notesLiveData.postValue(listOfNotes)
                }
        )
    }
}
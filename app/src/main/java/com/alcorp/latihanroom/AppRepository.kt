package com.alcorp.latihanroom

import android.app.Application
import androidx.lifecycle.LiveData
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class NoteRepository(application: Application) {
    private val mNotesDao: NoteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = NoteRoomDatabase.getDatabase(application)
        mNotesDao = db.noteDao()
    }

    fun getAllNotes(): LiveData<List<NoteEntity>> = mNotesDao.getAllNotes()

    fun insert(note: NoteEntity) {
        executorService.execute { mNotesDao.insert(note) }
    }

    fun deleteNoteById(noteId: Int) {
        mNotesDao.deleteById(noteId)
    }

    fun update(note: NoteEntity) {
        executorService.execute { mNotesDao.update(note) }
    }

    fun getAllDoneNotes(): LiveData<List<NoteEntity>> = mNotesDao.getAllDoneNotes()
}

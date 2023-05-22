package com.alcorp.latihanroom

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface NoteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(note: NoteEntity)

    @Update
    fun update(note: NoteEntity)

    @Delete
    fun delete(note: NoteEntity)

    @Query("DELETE FROM noteentity WHERE id = :noteId")
    fun deleteById(noteId: Int)

    @Query("SELECT * from noteentity WHERE isDone = 0 ORDER BY timeStart ASC")
    fun getAllNotes(): LiveData<List<NoteEntity>>

    @Query("SELECT * from noteentity WHERE isDone = 1 ORDER BY timeStart ASC")
    fun getAllDoneNotes(): LiveData<List<NoteEntity>>
}
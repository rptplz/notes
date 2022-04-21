package com.example.simplenotes;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface NoteDao {
    @Insert
    void insert(Note note);

    @Query("DELETE FROM note_table")
    void deleteAll();

    @Query("SELECT * FROM note_table ORDER BY title ASC")
    LiveData<List<Note>> getAllNotes();

    @Query("SELECT * FROM note_table LIMIT 1")
    Note[] getAnyNote();

    @Delete
    void deleteNote(Note note);

    @Update
    void update(Note... note);
}

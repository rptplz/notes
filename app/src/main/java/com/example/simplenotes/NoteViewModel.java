package com.example.simplenotes;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


/**
 * This class provides the interface between the UI and the data layer of the app,
 * represented by the NoteRepository
 */
public class NoteViewModel extends AndroidViewModel {
    private NoteRepository mRepository;
    private LiveData<List<Note>> mAllNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        mRepository = new NoteRepository(application);
        mAllNotes = mRepository.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public void insert(Note note) { mRepository.insert(note); }

    public void deleteAll() { mRepository.deleteAll(); }

    public void deleteNote(Note note) { mRepository.deleteNote(note); }

    public void update(Note note) { mRepository.update(note); }
}

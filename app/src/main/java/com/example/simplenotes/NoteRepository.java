package com.example.simplenotes;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class NoteRepository {
    private NoteDao mNoteDao;
    private LiveData<List<Note>> mAllNotes;

    public NoteRepository(Application application) {
        NoteRoomDatabase db = NoteRoomDatabase.getDatabase(application);
        mNoteDao = db.noteDao();
        mAllNotes = mNoteDao.getAllNotes();
    }

    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public void insert(Note note) {
        new InsertAsyncTask(mNoteDao).execute(note);
    }

    public void deleteAll() {
        new DeleteAllNotesAsyncTask(mNoteDao).execute();
    }

    public void deleteNote(Note note) {
        new DeleteNoteAsyncTask(mNoteDao).execute(note);
    }

    public void update(Note note) {
        new UpdateNoteAsyncTask(mNoteDao).execute(note);
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mAsyncTaskDao;

        UpdateNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... notes) {
            mAsyncTaskDao.update(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mAsyncTaskDao;

        DeleteNoteAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Note... notes) {
            mAsyncTaskDao.deleteNote(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NoteDao mAsyncTaskDao;

        DeleteAllNotesAsyncTask(NoteDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class InsertAsyncTask extends AsyncTask<Note, Void, Void> {
        private NoteDao mAsyncTaskDao;

        InsertAsyncTask(NoteDao mNoteDao) {
            mAsyncTaskDao = mNoteDao;
        }

        @Override
        protected Void doInBackground(final Note... notes) {
            mAsyncTaskDao.insert(notes[0]);
            return null;
        }
    }
}

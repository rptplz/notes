package com.example.simplenotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.LinkedList;
import java.util.List;

/**
 * This class displays a list of notes in a RecyclerView.
 * The notes are saved in a Room database.
 * The layout for this activity also displays a Floating Action Button
 * that allows users to start the EditNoteActivity to add new notes.
 * Users can delete a note by swiping it away, or delete all notes
 * through the options Menu.
 */


public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE_TITLE
            = "com.example.simplenotes.MainActivity.extra.NOTE_TITLE";
    public static final String EXTRA_NOTE_CONTENT
            = "com.example.simplenotes.MainActivity.extra.NOTE_CONTENT";
    public static final String EXTRA_NOTE_ID
            = "com.example.simplenotes.MainActivity.extra.NOTE_ID";

    public static final int REQUEST_NEW_NOTE = 1;
    public static final int REQUEST_EDIT_NOTE = 2;

    private RecyclerView mRecyclerView;
    private NoteListAdapter mAdapter;

    private NoteViewModel mNoteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);

        mRecyclerView = findViewById(R.id.recyclerview);
        mAdapter = new NoteListAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(final List<Note> notes) {
                mAdapter.setNotes(notes);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab_main);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditNoteActivity.class);
                MainActivity.this.startActivityForResult(intent, REQUEST_NEW_NOTE);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(
                new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                        int position = viewHolder.getAdapterPosition();
                        Note note = mAdapter.getNoteAtPosition(position);
                        showShortSnackBar(mRecyclerView, "Deleting '" + note.getTitle() + "'");
                        mNoteViewModel.deleteNote(note);
                    }
                }
        );

        helper.attachToRecyclerView(mRecyclerView);

        NoteListAdapter.setOnItemClickListener(new NoteListAdapter.ClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Note note = mAdapter.getNoteAtPosition(position);
                launchEditNoteActivity(note);
            }
        });

        NoteListAdapter.setOnMenuItemClickListener(new NoteListAdapter.MenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem, View itemView, int position) {
                switch (menuItem.getItemId()) {
                    case R.id.delete_note_action:
                        Note note = mAdapter.getNoteAtPosition(position);
                        mNoteViewModel.deleteNote(note);
                        return true;
                    case R.id.edit_note_action:
                        return itemView.callOnClick();

                }
                return false;
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_NEW_NOTE) {
                String newNoteTitle = data.getStringExtra(EditNoteActivity.EXTRA_NOTE_TITLE_EDITED);
                String newNoteContent = data.getStringExtra(EditNoteActivity.EXTRA_NOTE_CONTENT_EDITED);
                mNoteViewModel.insert(new Note(newNoteTitle, newNoteContent));
            }

            if (requestCode == REQUEST_EDIT_NOTE) {
                String editedNoteTitle = data.getStringExtra(EditNoteActivity.EXTRA_NOTE_TITLE_EDITED);
                String editedNoteContent = data.getStringExtra(EditNoteActivity.EXTRA_NOTE_CONTENT_EDITED);
                int noteId = data.getIntExtra(EditNoteActivity.EXTRA_REPLY_ID, -1);

                if (noteId != -1) {
                    mNoteViewModel.update(new Note(noteId, editedNoteTitle, editedNoteContent));
                } else {
                    showShortSnackBar(mRecyclerView, getString(R.string.unable_edit_note));
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_main, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_action).getActionView();
        setupSearch(searchView);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.delete_all_action) {
            showDeleteAllAlertDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSearch(SearchView searchView) {
        searchView.setQueryHint("Search for note");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchString) {
                mAdapter.getFilter().filter(searchString);
                return true;
            }
        });
    }

    private void showDeleteAllAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Delete all notes")
                .setMessage("Are you sure you want to delete all notes?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        mNoteViewModel.deleteAll();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }

    private void launchEditNoteActivity(Note note) {
        Intent intent = new Intent(this, EditNoteActivity.class);
        intent.putExtra(EXTRA_NOTE_TITLE, note.getTitle());
        intent.putExtra(EXTRA_NOTE_CONTENT, note.getContent());
        intent.putExtra(EXTRA_NOTE_ID, note.getId());
        startActivityForResult(intent, REQUEST_EDIT_NOTE);
    }

    private void showShortSnackBar(View view, String message) {
        Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
    }

}
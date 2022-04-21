package com.example.simplenotes;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


/**
 * This class displays a screen where the user edits an existing note
 * or creates new one. The EditNoteActivity returns the edited note
 * to the calling activity (MainActivity), which saves the edited note
 * and updates the list of displayed notes.
 */

public class EditNoteActivity extends AppCompatActivity {
    public static final String EXTRA_NOTE_TITLE_EDITED = "com.example.simplenotes.EditNoteActivity.NOTE_TITLE_EDITED";
    public static final String EXTRA_NOTE_CONTENT_EDITED = "com.example.simplenotes.EditNoteActivity.NOTE_CONTENT_EDITED";
    public static final String EXTRA_REPLY_ID = "com.example.simplenotes.EditNoteActivity.NOTE_ID";

    private EditText mNoteTitleEditText;
    private EditText mNoteContentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        Toolbar toolbar = findViewById(R.id.toolbar_edit_note);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mNoteTitleEditText = findViewById(R.id.edittext_note_title);
        mNoteContentEditText = findViewById(R.id.edittext_note_content);

        final Bundle extras = getIntent().getExtras();

        if (extras != null) {
            String noteTitle = extras.getString(MainActivity.EXTRA_NOTE_TITLE);
            String noteContent = extras.getString(MainActivity.EXTRA_NOTE_CONTENT);
            mNoteTitleEditText.setText(noteTitle == null ? "" : noteTitle);
            mNoteContentEditText.setText(noteContent == null ? "" : noteContent);
        }

        FloatingActionButton fab = findViewById(R.id.fab_edit_note);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String editedTitle = mNoteTitleEditText.getText().toString();
                String editedContent = mNoteContentEditText.getText().toString();
                Intent replyIntent = new Intent();
                replyIntent.putExtra(EXTRA_NOTE_TITLE_EDITED, editedTitle);
                replyIntent.putExtra(EXTRA_NOTE_CONTENT_EDITED, editedContent);
                if (extras != null && extras.containsKey(MainActivity.EXTRA_NOTE_ID)) {
                    int id = extras.getInt(MainActivity.EXTRA_NOTE_ID, -1);
                    if (id != -1) {
                        replyIntent.putExtra(EXTRA_REPLY_ID, id);
                    }
                }
                setResult(Activity.RESULT_OK, replyIntent);
                finish();
            }
        });
    }
}
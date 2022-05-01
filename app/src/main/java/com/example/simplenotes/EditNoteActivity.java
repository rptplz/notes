package com.example.simplenotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.SpannedString;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.SearchView;

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
            SpannableString spannableString = new SpannableString((Html.fromHtml(noteContent)));
            mNoteTitleEditText.setText(noteTitle == null ? "" : noteTitle);
            mNoteContentEditText.setText(noteContent == null ? "" : spannableString);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_options_edit_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_apply) {
            sendReplyIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendReplyIntent() {
        Bundle extras = getIntent().getExtras();
        String editedTitle = mNoteTitleEditText.getText().toString();
        String editedContent = Html.toHtml(mNoteContentEditText.getText());
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

    public void formatTextBold(View view) {
        int selectionStart = mNoteContentEditText.getSelectionStart();
        int selectionEnd = mNoteContentEditText.getSelectionEnd();

        if (selectionStart == selectionEnd) return;

        boolean isTextBold = false;

        Spannable spannableString =
                new SpannableStringBuilder(mNoteContentEditText.getText());

        StyleSpan[] spans = spannableString
                .getSpans(selectionStart,
                        selectionEnd, StyleSpan.class);

        for (StyleSpan span : spans) {
            if (span.getStyle() == Typeface.BOLD) {
                isTextBold = true;
                spannableString.removeSpan(span);
                break;
            }
        }

        if (!isTextBold) {
            spannableString.setSpan(new StyleSpan(Typeface.BOLD), selectionStart,
                    selectionEnd, 0);
        }

        mNoteContentEditText.setText(spannableString);
    }

    public void formatTextItalic(View view) {
        int selectionStart = mNoteContentEditText.getSelectionStart();
        int selectionEnd = mNoteContentEditText.getSelectionEnd();

        if (selectionStart == selectionEnd) return;

        boolean isTextItalic = false;

        Spannable spannableString =
                new SpannableStringBuilder(mNoteContentEditText.getText());

        StyleSpan[] spans = spannableString
                .getSpans(selectionStart,
                        selectionEnd, StyleSpan.class);

        for (StyleSpan span : spans) {
            if (span.getStyle() == Typeface.ITALIC) {
                isTextItalic = true;
                spannableString.removeSpan(span);
                break;
            }
        }

        if (!isTextItalic) {
            spannableString.setSpan(
                    new StyleSpan(Typeface.ITALIC), selectionStart,
                    selectionEnd, 0);
        }

        mNoteContentEditText.setText(spannableString);
    }

    public void formatTextUnderlined(View view) {
        int selectionStart = mNoteContentEditText.getSelectionStart();
        int selectionEnd = mNoteContentEditText.getSelectionEnd();

        if (selectionStart == selectionEnd) return;

        boolean isTextUnderlined = false;

        Spannable spannableString =
                new SpannableStringBuilder(mNoteContentEditText.getText());

        UnderlineSpan[] spans = spannableString
                .getSpans(selectionStart,
                        selectionEnd, UnderlineSpan.class);

        if (spans.length != 0) {
            isTextUnderlined = true;

            for (UnderlineSpan span : spans) {
                spannableString.removeSpan(span);
            }
        }

        if (!isTextUnderlined) {
            spannableString.setSpan(
                    new UnderlineSpan(),
                    selectionStart,
                    selectionEnd, 0);

        }

        mNoteContentEditText.setText(spannableString);

    }
}
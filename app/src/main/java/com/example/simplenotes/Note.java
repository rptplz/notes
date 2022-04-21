package com.example.simplenotes;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;

/**
 * Entity class that represents a note in the database.
 */

@Entity(tableName = "note_table")
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @NonNull
    @ColumnInfo(name = "title")
    private String title;

    @NonNull
    @ColumnInfo(name = "content")
    private String content;

    public Note(@NonNull String title, @NonNull String content) {
        this.title = title;
        this.content = content;
    }


    /*
     * This constructor is annotated using @Ignore, because Room expects only
     * one constructor by default in an entity class.
     */
    @Ignore
    public Note(int id, @NonNull String title, @NonNull String content) {
        this.id = id;
        this.title = title;
        this.content = content;
    }

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
}

package com.example.simplenotes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

/**
 * Adapter for the RecyclerView that displays a list of notes.
 */

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {
    private static ClickListener clickListener;
    private static MenuItemClickListener menuItemClickListener;
    private List<Note> mNoteList;
    private LayoutInflater mInflater;

    public NoteListAdapter(Context context) {
        this.mInflater = LayoutInflater.from(context);
    }

    class NoteViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, PopupMenu.OnMenuItemClickListener {
        public final TextView noteTitleView;
        public final TextView noteContentView;
        final NoteListAdapter mAdapter;

        public NoteViewHolder(@NonNull View itemView, NoteListAdapter mAdapter) {
            super(itemView);
            this.noteTitleView = itemView.findViewById(R.id.note_title);
            this.noteContentView = itemView.findViewById(R.id.note_content);
            this.mAdapter = mAdapter;
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickListener.onItemClick(view, getAdapterPosition());
                }
            });
        }

        @Override
        public boolean onLongClick(View view) {
            PopupMenu popup = new PopupMenu(itemView.getContext(), view);
            popup.setOnMenuItemClickListener(this);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.menu_popup_notes, popup.getMenu());
            popup.show();
            return true;
        }


        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            return menuItemClickListener.onMenuItemClick(menuItem, itemView, getAdapterPosition());
        }
    }

    @NonNull
    @Override
    public NoteListAdapter.NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mItemView = mInflater.inflate(R.layout.notelist_item, parent, false);
        return new NoteViewHolder(mItemView, this);
    }


    @Override
    public void onBindViewHolder(@NonNull NoteListAdapter.NoteViewHolder holder, int position) {
        if (mNoteList != null) {
            Note mCurrent = mNoteList.get(position);
            holder.noteTitleView.setText(mCurrent.getTitle());
            holder.noteContentView.setText(getCompactNoteContent(mCurrent.getContent()));
        }
    }


    @Override
    public int getItemCount() {
        if (mNoteList != null) {
            return mNoteList.size();
        } else return 0;
    }

    void setNotes(List<Note> notes) {
        mNoteList = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAtPosition(int position) {
        return mNoteList.get(position);
    }

    private String getCompactNoteContent(String content) {
        return content.length() > 300
                ? content.substring(0, 300) + "..."
                : content;
    }

    public static void setOnItemClickListener(ClickListener clickListener) {
        NoteListAdapter.clickListener = clickListener;
    }

    public static void setOnMenuItemClickListener(MenuItemClickListener menuItemClickListener) {
        NoteListAdapter.menuItemClickListener = menuItemClickListener;
    }

    public interface ClickListener {
        void onItemClick(View view, int position);
    }

    public interface MenuItemClickListener {
        boolean onMenuItemClick(MenuItem menuItem, View itemView, int position);
    }
}

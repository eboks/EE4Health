package com.empatica.sample.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.empatica.sample.R;
import com.empatica.sample.activities.MainActivity;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.fragments.StudentFragment;
import com.empatica.sample.models.Note;
import com.empatica.sample.models.Student;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder>{
    protected List<Note> notes = new ArrayList<>();
    private OnItemClickListener listener;


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_main, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Note currentNote = notes.get(position);
        Log.d(TAG, "onClick: "+currentNote.getNoteTitle());

        holder.textViewTitle.setText(currentNote.getNoteTitle());
/*
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+currentNote.getNoteTitle());
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return notes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView mCardView;
        TextView textViewTitle;
        //NoteAdapter.OnNoteListener onNoteListener;

        public ViewHolder(@NonNull View itemView){
            super(itemView);
            mCardView = (CardView)  itemView.findViewById(R.id.note_card);
            textViewTitle = itemView.findViewById(R.id.text_view_note_title);
           // this.onNoteListener = onNoteListener;
            itemView.setOnClickListener(this);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position = getAdapterPosition();
                    if(listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(notes.get(position));
                    }

                }
            });
        }

        @Override
        public void onClick(View view) {
        }

    }

    public interface OnNoteListener{
        void onNoteClick(int position);
    }

    public void setNotes(List<Note> notes){
        this.notes = notes;
        notifyDataSetChanged();
    }

    public Note getNoteAt(int position){
        return notes.get(position);
    }


    public interface OnItemClickListener {
        void onItemClick(Note note);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        this.listener = listener;

    }


}

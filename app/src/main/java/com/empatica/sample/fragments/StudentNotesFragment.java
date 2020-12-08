package com.empatica.sample.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empatica.sample.R;
import com.empatica.sample.activities.AddNoteActivity;
import com.empatica.sample.adapter.NoteAdapter;
import com.empatica.sample.adapter.StudentAdapter;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.models.Note;
import com.empatica.sample.models.Student;
import com.empatica.sample.viewModels.NoteViewModel;
import com.empatica.sample.viewModels.StudentViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class StudentNotesFragment  extends Fragment {

    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;

    private FloatingActionButton fab;
    private View studentNotesView;
    private Student student;
    private RecyclerView.Adapter adapter;
    private NoteAdapter noteAdapter;
    private NoteViewModel noteViewModel;
    private List<Note> notes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        studentNotesView = inflater.inflate(R.layout.fragment_student_notes, container, false);
        fab = studentNotesView.findViewById(R.id.fab_add_note);

        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> {
            Toast.makeText(getContext(), "onChanged", Toast.LENGTH_SHORT);          //  noteAdapter.setNotes(notes);
        });

        RecyclerView recyclerView = studentNotesView.findViewById(R.id.recycler_view_notes);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(false);

        final NoteAdapter adapter = new NoteAdapter();
        recyclerView.setAdapter(adapter);

        noteViewModel = ViewModelProviders.of(this.getParentFragment()).get(NoteViewModel.class);
        noteViewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> adapter.setNotes(notes));

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            student = (Student) bundle.getSerializable("student");
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("student", student);
                Intent intent = new Intent(getContext(), AddNoteActivity.class);
                startActivityForResult(intent, ADD_NOTE_REQUEST );
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                noteViewModel.delete(adapter.getNoteAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                Intent intent = new Intent(getContext(), AddNoteActivity.class);
                intent.putExtra(AddNoteActivity.EXTRA_TITLE, note.getNoteTitle());
                intent.putExtra(AddNoteActivity.EXTRA_DESCRIPTION, note.getNoteContent());
                intent.putExtra(AddNoteActivity.EXTRA_TIMESTAMP, note.getDateTime());
                intent.putExtra(AddNoteActivity.EXTRA_ID, note.getId());

                startActivityForResult(intent, EDIT_NOTE_REQUEST);

            }
        });
        return studentNotesView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK){
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            String dateTime = data.getStringExtra(AddNoteActivity.EXTRA_TIMESTAMP);

            Note note = new Note();
            note.setDateTime(dateTime);
            note.setNoteContent(description);
            note.setNoteTitle(title);
            note.setStudentId(student.getId());
            noteViewModel.insert(note);

            Toast.makeText(getContext(), "Note saved", Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK){

            int id = data.getIntExtra(AddNoteActivity.EXTRA_ID, -1);
            if(id != -1){
                Toast.makeText(getContext(), "Note cannot be updated", Toast.LENGTH_SHORT).show();

            }
            String title = data.getStringExtra(AddNoteActivity.EXTRA_TITLE);
            String description = data.getStringExtra(AddNoteActivity.EXTRA_DESCRIPTION);
            String dateTime = data.getStringExtra(AddNoteActivity.EXTRA_TIMESTAMP);

            Note note = new Note();
            note.setId(id);
            note.setDateTime(dateTime);
            note.setNoteContent(description);
            note.setNoteTitle(title);
            noteViewModel.update(note);

            Toast.makeText(getContext(), "Note saved", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Note not saved", Toast.LENGTH_SHORT).show();

        }
    }

    public void getNotes(){

        class GetNotesTask extends AsyncTask<Void, Void, LiveData<List<Note>>>{

            @Override
            protected LiveData<List<Note>> doInBackground(Void... voids) {
                return RoomDB.getInstance(getContext()).noteDao().getAllNotes();
            }
        }
    }

    public void onNoteClick(int position) {

        Log.d(TAG,"CLICKED"+noteAdapter.getNoteAt(position).getId());



    }
}

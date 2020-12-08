package com.empatica.sample.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;

import com.empatica.sample.R;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.models.Note;
import com.empatica.sample.models.Student;
import com.empatica.sample.viewModels.NoteViewModel;
import com.empatica.sample.viewModels.StudentViewModel;
import com.fuzzylite.hedge.Not;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteFragment extends Fragment {

    private Button btnAdd, btnCancel;
    private View addNoteView;
    private EditText editTitle, editContent;
    private TextView textDateTime;
    private NoteViewModel noteViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addNoteView = inflater.inflate(R.layout.fragment_note_add, container, false);
        initVar();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNote();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Navigation.findNavController(getActivity(), R.id.nav_host_fragment_container).navigate(R.id.addNoteFragment);

            }
        });
        return addNoteView;
    }



    private void addNote(){
        String noteTitle = editTitle.getText().toString();
        String noteContent = editContent.getText().toString();
        String dateTime = textDateTime.getText().toString();

        if(noteTitle.trim().isEmpty() || noteContent.trim().isEmpty()){
            Toast.makeText(getContext(), "Please insert a note title and some content for it.",Toast.LENGTH_SHORT ).show();
            return;
        }
        Note note = new Note();
        note.setDateTime(dateTime);
        note.setNoteContent(noteContent);
        note.setNoteTitle(noteTitle);
        note.setStudentId(1);
        noteViewModel.insert(note);

    }



    private void initVar(){
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        btnAdd = addNoteView.findViewById(R.id.button_add_note);
        btnCancel = addNoteView.findViewById(R.id.button_cancel);
        editTitle = addNoteView.findViewById(R.id.edit_text_note_title);
        editContent = addNoteView.findViewById(R.id.edit_text_note_content);
        textDateTime = addNoteView.findViewById(R.id.text_date_time);
        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd-MMMM-yyyy HH:mm a", Locale.getDefault())
                .format(new Date())
        );
    }


}

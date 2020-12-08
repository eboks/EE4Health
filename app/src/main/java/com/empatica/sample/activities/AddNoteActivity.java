package com.empatica.sample.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.empatica.sample.R;
import com.empatica.sample.models.Note;
import com.empatica.sample.viewModels.NoteViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddNoteActivity extends AppCompatActivity {
    public static final String EXTRA_ID = "com.empatica.sample.activities.EXTRA_ID";

    public static final String EXTRA_TITLE = "com.empatica.sample.activities.EXTRA_TITLE";
    public static final String EXTRA_DESCRIPTION = "com.empatica.sample.activities.EXTRA_DESCRIPTION";
    public static final String EXTRA_TIMESTAMP = "com.empatica.sample.activities.EXTRA_TIMESTAMP";

    private EditText editTitle, editContent;
    private TextView textDateTime;
    private NoteViewModel noteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        initVar();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        Intent intent = getIntent();

        if(intent.hasExtra(EXTRA_ID)){
            setTitle("Edit Note");
            editTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editContent.setText((intent.getStringExtra(EXTRA_DESCRIPTION)));
            textDateTime.setText(intent.getStringExtra(EXTRA_TIMESTAMP));
        }
        else {
            setTitle("Add Note");
        }
    }

    private void saveNote(){
        String noteTitle = editTitle.getText().toString();
        String noteContent = editContent.getText().toString();
        String dateTime = textDateTime.getText().toString();

        if(noteTitle.trim().isEmpty() || noteContent.trim().isEmpty()){
            Toast.makeText(this, "Please insert a note title and some content for it.",Toast.LENGTH_SHORT ).show();
            return;
        }
        /*Note note = new Note();
        note.setDateTime(dateTime);
        note.setNoteContent(noteContent);
        note.setNoteTitle(noteTitle);
        note.setStudentId(1);
        noteViewModel.insert(note);*/
        Intent data = new Intent();
        data.putExtra(EXTRA_TITLE, noteTitle);
        data.putExtra(EXTRA_DESCRIPTION, noteContent);
        data.putExtra(EXTRA_TIMESTAMP, dateTime);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if(id != -1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.save_note:
                saveNote();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void initVar(){
        editTitle = findViewById(R.id.edit_text_note_title);
        editContent = findViewById(R.id.edit_text_note_content);
        textDateTime = findViewById(R.id.text_date_time);
        textDateTime.setText(
                new SimpleDateFormat("EEEE, dd-MMMM-yyyy HH:mm a", Locale.getDefault())
                        .format(new Date())
        );
    }

}
package com.empatica.sample.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empatica.sample.R;
import com.empatica.sample.adapter.StudentAdapter;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.models.Student;

import java.util.ArrayList;
import java.util.List;

public class ClassroomActivity  extends AppCompatActivity {

    EditText editText;
    Button btnAdd, btnReset;
    RecyclerView recyclerView;

    List<Student> studentList = new ArrayList<Student>();
    LinearLayoutManager linearLayoutManager;
    RoomDB database;
    StudentAdapter studentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.classroom);

        editText = findViewById(R.id.edit_text);
        btnAdd = findViewById(R.id.button_add);
        btnReset = findViewById(R.id.button_reset);
        recyclerView = findViewById(R.id.recycler_view);

        //Initialize database

        database = RoomDB.getInstance(this);

        // Store database value in student list
        studentList = database.studentDao().getAll();

        //Initialize linear layout manager
        linearLayoutManager = new LinearLayoutManager(this);
        //Set layout manager
        recyclerView.setLayoutManager(linearLayoutManager);
        studentAdapter = new StudentAdapter(ClassroomActivity.this, studentList);

        //set adapter
        recyclerView.setAdapter(studentAdapter);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Get string from edit text
                String sText = editText.getText().toString().trim();
                //Check condition
                if(!sText.equals("")){
                    //text not empty
                    //intiliaze student
                    Student student = new Student();
                    //Set text on student
                    student.setFirstName(sText);
                    //Insert text in database
                    database.studentDao().insert(student);
                    //Clear edit text
                    editText.setText("");
                    //Notify when text is inserted
                    studentList.clear();
                    studentList.addAll(database.studentDao().getAll());
                    studentAdapter.notifyDataSetChanged();

                }
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Delete all data from database
                database.studentDao().reset(studentList);
                //Notify when all students are deleted
                studentList.clear();
                studentList.addAll(database.studentDao().getAll());
                studentAdapter.notifyDataSetChanged();
            }
        });

    }
}

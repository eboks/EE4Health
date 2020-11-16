package com.empatica.sample.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.empatica.sample.R;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.fragments.AddStudentFragment;
import com.empatica.sample.fragments.ClassroomFragment;
import com.empatica.sample.fragments.SettingsFragment;
import com.empatica.sample.fragments.TeacherFragment;
import com.empatica.sample.models.Teacher;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    Toolbar toolbar;

/*    EditText editText;
    Button btnAdd, btnReset;
    RecyclerView recyclerView;
    List<Student> studentList = new ArrayList<Student>();
    LinearLayoutManager linearLayoutManager;
    StudentAdapter studentAdapter;*/
RoomDB database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_m);

        initVar();
        setSupportActionBar(toolbar);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle  = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new ClassroomFragment()).commit();
        navigationView.setCheckedItem(R.id.nav_classroom);}



        //Initialize database
        database = RoomDB.getInstance(this);

        //getCurrentTeacher
        if(getIntent().getExtras() != null){
            Teacher teacher = (Teacher) getIntent().getSerializableExtra("teacher");
        }
/*
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
                    //initialize student
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
        });*/

       /* btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Delete all data from database
                database.studentDao().reset(studentList);
                //Notify when all students are deleted
                studentList.clear();
                studentList.addAll(database.studentDao().getAll());
                studentAdapter.notifyDataSetChanged();
            }
        });*/

    }

    private void initVar(){
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
/*        editText = findViewById(R.id.edit_text);
        btnAdd = findViewById(R.id.button_add);
        btnReset = findViewById(R.id.button_reset);
        recyclerView = findViewById(R.id.recycler_view);*/
    }

    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_classroom:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ClassroomFragment()).commit();
                break;

            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new SettingsFragment()).commit();
                break;
            case R.id.nav_myprofile:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TeacherFragment()).commit();
                break;
            case R.id.nav_add_person:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new AddStudentFragment()).commit();
                break;
        }
        return true;
    }
}

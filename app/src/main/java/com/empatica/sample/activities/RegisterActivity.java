package com.empatica.sample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.empatica.sample.R;
import com.empatica.sample.dao.TeacherDao;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.models.Student;
import com.empatica.sample.models.Teacher;
import com.empatica.sample.viewModels.StudentViewModel;
import com.empatica.sample.viewModels.TeacherViewModel;

public class RegisterActivity extends AppCompatActivity {
    EditText firstName, lastName, email, password;
    Button btnLogin, btnRegister;
    private TeacherViewModel teacherViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initVar();

        teacherViewModel = ViewModelProviders.of(this).get(TeacherViewModel.class);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Teacher teacher = new Teacher();
                teacher.setEmail(email.getText().toString().trim());
                teacher.setFirstName(firstName.getText().toString().trim());
                teacher.setLastName(lastName.getText().toString().trim());
                teacher.setPassword(password.getText().toString().trim());

                if(validateInput(teacher)){
                    teacherViewModel.insert(teacher);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(getApplicationContext(),"User registered",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                }
                            });
                        }
                    }).start();
                }else{
                    Toast.makeText(getApplicationContext(), "Fill all fields", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });

    }

    private Boolean validateInput(Teacher teacher){
        if(teacher.getEmail().isEmpty() ||
        teacher.getFirstName().isEmpty() ||
        teacher.getLastName().isEmpty() ||
        teacher.getPassword().isEmpty()) return false;

        return true;
    }

    private void initVar(){
        firstName = findViewById(R.id.first_name);
        lastName = findViewById(R.id.last_name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        btnLogin = findViewById(R.id.button_cancel);
        btnRegister = findViewById(R.id.button_register);
    }
}

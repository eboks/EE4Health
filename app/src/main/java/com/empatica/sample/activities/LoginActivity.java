package com.empatica.sample.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import com.empatica.sample.R;
import com.empatica.sample.dao.TeacherDao;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.models.Teacher;
import com.empatica.sample.viewModels.TeacherViewModel;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button btnLogin, btnRegister;

    private TeacherViewModel teacherViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        initVar();

     //   teacherViewModel = ViewModelProviders.of(this).get(TeacherViewModel.class);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String emailText = email.getText().toString().trim();
                final String passwordText = password.getText().toString().trim();
                if(emailText.isEmpty() || passwordText.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill all fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    //Perform Query
                    RoomDB database = RoomDB.getInstance(getApplicationContext());
                    final TeacherDao teacherdao = database.teacherDao();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Teacher teacher = teacherdao.login(emailText, passwordText);
                            if(teacher == null){
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(getApplicationContext(), "Invalid credentials", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                intent.putExtra("teacher", teacher);
                                startActivity(intent);

                            }

                        }
                    }).start();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

    }

    private void initVar(){
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);

        btnLogin = findViewById(R.id.button_login);
        btnRegister = findViewById(R.id.button_register);
    }

}

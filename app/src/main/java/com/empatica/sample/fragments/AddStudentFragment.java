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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.empatica.sample.R;

public class AddStudentFragment extends Fragment {
    public static final String EXTRA_FIRSTNAME =
            "com.empatica.sample.fragments";
    public static final String EXTRA_LASTNAME =
            "com.empatica.sample.fragments";

    private Button btnAdd, btnCancel;
    private View addStudentView;
    private EditText editTextFirstName, editTextLastName;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        addStudentView = inflater.inflate(R.layout.fragment_student_add, container, false);
        initVar();

        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close);

        ((AppCompatActivity)getActivity()).setTitle("Add student");

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addStudent();
            }
        });


        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });
        return addStudentView;
    }



    private void addStudent(){
        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();

        if(firstName.trim().isEmpty() || lastName.trim().isEmpty()){
            Toast.makeText(getContext(), "Please insert a first name and last name.",Toast.LENGTH_SHORT ).show();
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_FIRSTNAME, firstName);
        data.putExtra(EXTRA_LASTNAME, lastName);

        ((AppCompatActivity)getActivity()).setResult(Activity.RESULT_OK, data);
        ((AppCompatActivity)getActivity()).finish();
    }



    private void initVar(){
        btnAdd = addStudentView.findViewById(R.id.button_add_student);
        btnCancel = addStudentView.findViewById(R.id.button_cancel);
        editTextFirstName = addStudentView.findViewById(R.id.edit_text_first_name);
        editTextLastName = addStudentView.findViewById(R.id.edit_text_last_name);
    }


}

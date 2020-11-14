package com.empatica.sample.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.empatica.sample.R;

public class ListNotes extends AppCompatActivity {
    //initialise variable
    EditText editText;
    Button btAdd,btReset;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listnotes);

        //assign variable

    }
}
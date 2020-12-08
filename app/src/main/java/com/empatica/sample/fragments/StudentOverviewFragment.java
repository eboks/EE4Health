package com.empatica.sample.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.empatica.sample.R;
import com.empatica.sample.adapter.ViewPageAdapter;
import com.empatica.sample.models.Student;
import com.google.android.material.tabs.TabLayout;

import static android.content.ContentValues.TAG;

public class StudentOverviewFragment  extends Fragment {

    private View studentOverviewView;
    private TextView textView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        studentOverviewView = inflater.inflate(R.layout.fragment_student_overview, container, false);
       // textView = studentOverviewView.findViewById(R.id.dummy);

        Bundle bundle = this.getArguments();

        if (bundle != null) {
            Student student = (Student) bundle.getSerializable("student");
            Log.d(TAG,"overview calling id:"+student.getId());
           // textView.setText(student.getFirstName()+" "+student.getLastName());
        }

        return studentOverviewView;

    }



}

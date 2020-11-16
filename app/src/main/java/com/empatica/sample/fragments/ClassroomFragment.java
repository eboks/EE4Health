package com.empatica.sample.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empatica.sample.R;
import com.empatica.sample.adapter.StudentAdapter;
import com.empatica.sample.models.Student;
import com.empatica.sample.viewModels.StudentViewModel;

import java.util.List;

public class ClassroomFragment extends Fragment {

    private StudentViewModel studentViewModel;
    private View classroomView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        classroomView = inflater.inflate(R.layout.fragment_classroom, container, false);
        recyclerView = classroomView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        final StudentAdapter studentAdapter = new StudentAdapter();
        recyclerView.setAdapter(studentAdapter);


        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        studentViewModel.getAllStudents().observe(getViewLifecycleOwner(), new Observer<List<Student>>(){
            @Override
            public void onChanged(List<Student> students) {
                //update RecyclerView
                studentAdapter.setStudents(students);
            }
        });
        return classroomView;
    }
}
package com.empatica.sample.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.empatica.sample.R;
import com.empatica.sample.adapter.StudentAdapter;
import com.empatica.sample.models.Student;
import com.empatica.sample.viewModels.StudentViewModel;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class ClassroomFragment extends Fragment implements StudentAdapter.OnStudentListener  {
    private List<Student> students = new ArrayList<Student>();
    private StudentViewModel studentViewModel;
    private View classroomView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private StudentAdapter studentAdapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //getActivity().setTitle("Class");

        classroomView = inflater.inflate(R.layout.fragment_classroom, container, false);
        recyclerView = classroomView.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setHasFixedSize(true);

        studentAdapter = new StudentAdapter(this::onStudentClick, getContext());
        recyclerView.setAdapter(studentAdapter);


        studentViewModel = ViewModelProviders.of(this).get(StudentViewModel.class);
        studentViewModel.getAllStudents().observe(getViewLifecycleOwner(), students -> {
            //update RecyclerView
            studentAdapter.setStudents(students);
        });


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                studentViewModel.delete(studentAdapter.getStudentAt(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);
        return classroomView;
    }

    @Override
    public void onStudentClick(int position) {
       // StudentOverviewFragment fragment = new StudentOverviewFragment();
       // Bundle bundle = new Bundle();
        //bundle.putInt("id", studentAdapter.getStudentAt(position).getId());
        Log.d(TAG,"CLICKED"+studentAdapter.getStudentAt(position).getId());


       /* fragment.setArguments(bundle);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_container, fragment, "studentFragment")
                .addToBackStack(null)
                .commit();*/

    }
}
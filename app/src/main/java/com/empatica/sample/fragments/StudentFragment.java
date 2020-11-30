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

public class StudentFragment  extends Fragment {
    TextView textViewFullName;
    TabLayout tabLayout;
    ViewPager viewPager;
    View studentView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        studentView = inflater.inflate(R.layout.fragment_student, container, false);

        textViewFullName = studentView.findViewById(R.id.text_view_full_name);

        viewPager = studentView.findViewById(R.id.view_pager);
        setUpViewPager(viewPager);

        tabLayout = studentView.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            Student student = (Student) bundle.getSerializable("student");
            Log.d(TAG,"id:"+student.getId());
            textViewFullName.setText(student.getFirstName()+" "+student.getLastName());

        }
        return studentView;

    }

    private void setUpViewPager(ViewPager viewPager){
        ViewPageAdapter adapter = new ViewPageAdapter(getActivity().getSupportFragmentManager());

        adapter.addFragment(new StudentOverviewFragment(), "OVERVIEW");
        adapter.addFragment(new StudentNotesFragment(), "NOTES");
        viewPager.setAdapter(adapter);
    }


}

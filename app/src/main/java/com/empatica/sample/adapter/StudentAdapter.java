package com.empatica.sample.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.empatica.sample.R;
import com.empatica.sample.activities.LoginActivity;
import com.empatica.sample.activities.MainActivity;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.fragments.StudentFragment;
import com.empatica.sample.fragments.StudentOverviewFragment;
import com.empatica.sample.models.Student;
import com.empatica.sample.models.Teacher;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import static android.content.ContentValues.TAG;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    protected List<Student> students = new ArrayList<Student>();
    private Activity context;
    private RoomDB database;
    private OnStudentListener mOnStudentListener;
    private Student currentStudent;


    public StudentAdapter( OnStudentListener onStudentListener){
        this.mOnStudentListener = onStudentListener;
        /**/


    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student,parent, false);


        return new ViewHolder(view, mOnStudentListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        currentStudent = students.get(position);

       // if(currentStudent.getStressLevel()==0)
      //  holder.mCardView.setCardBackgroundColor(Color.GREEN); // will change the background color of the card view to green
      //  else         holder.mCardView.setCardBackgroundColor(Color.RED); // will change the background color of the card view to green

        holder.textViewFirstName.setText(currentStudent.getFirstName()+" "+currentStudent.getLastName());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: "+currentStudent.getLastName());
                Bundle bundle = new Bundle();
                bundle.putSerializable("student", currentStudent);
                AppCompatActivity appCompatActivity = (AppCompatActivity)view.getContext();
                StudentFragment studentFragment = new StudentFragment();
                studentFragment.setArguments(bundle);
                appCompatActivity.getSupportFragmentManager().beginTransaction().replace(R.id.frame, studentFragment).addToBackStack(null).commit();
            }
        });
    }


    @Override
    public int getItemCount() {
        return students.size();
    }

    public void setStudents(List<Student> students){
        this.students = students;
        notifyDataSetChanged();
    }

    public Student getStudentAt(int position){
        return students.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        CardView mCardView;
        TextView textViewFirstName;
        OnStudentListener onStudentListener;

        public ViewHolder(@NonNull View itemView, OnStudentListener onStudentListener){
            super(itemView);
            mCardView = (CardView)  itemView.findViewById(R.id.student_card);
            textViewFirstName= itemView.findViewById(R.id.text_view_first_name);
           // Timer timer = new Timer();
           // MyTimer myTimer = new MyTimer();
            //timer.schedule(myTimer, 2000, 2000);
            this.onStudentListener = onStudentListener;
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onStudentListener.onStudentClick(getAdapterPosition());
        }

      /*  private class MyTimer extends TimerTask {
            @Override
            public void run() {

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if(currentStudent.getStressLevel()==0){
                            mCardView.setCardBackgroundColor(Color.GREEN);
                            currentStudent.setStressLevel(1);
                        }
                        else {
                            mCardView.setCardBackgroundColor(Color.RED);
                            currentStudent.setStressLevel(0);
                        }
                    }
                }).start();
            }
        }*/
    }

    public interface OnStudentListener{
        void onStudentClick(int position);
    }



}

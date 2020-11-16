package com.empatica.sample.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.empatica.sample.R;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.models.Student;

import java.util.ArrayList;
import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<Student> students = new ArrayList<Student>();
    private Activity context;
    private RoomDB database;



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.student,parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        Student currrentStudent = students.get(position);
        holder.textViewFirstName.setText(currrentStudent.getFirstName());

        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textViewFirstName;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            textViewFirstName= itemView.findViewById(R.id.text_view_first_name);
            btnDelete = itemView.findViewById(R.id.button_delete);
            btnEdit = itemView.findViewById(R.id.button_eddit);
        }
    }
}

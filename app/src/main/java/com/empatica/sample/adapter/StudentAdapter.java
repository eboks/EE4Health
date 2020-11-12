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

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {
    private List<Student> studentList;
    private Activity context;
    private RoomDB database;

    public StudentAdapter(Activity context, List<Student> studentList){
        this.context = context;
        this.studentList = studentList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_student,parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        //Initialize student
        final Student student = studentList.get(position);
        //Initialize database
        database = RoomDB.getInstance(context);
        //Set text on text view
        holder.textView.setText(student.getFirstName());
        holder.btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Initialize student
                Student s = studentList.get(holder.getAdapterPosition());

                //get id
                final int sID = s.getId();
                //get first name
                String sText = s.getFirstName();

                //Create dialog
                final Dialog dialog = new Dialog(context);

                //Set content view
                dialog.setContentView(R.layout.dialog_update);

                //init width
                int width = WindowManager.LayoutParams.MATCH_PARENT;

                //init height
                int height = WindowManager.LayoutParams.WRAP_CONTENT;

                //Set layout
                dialog.getWindow().setLayout(width, height);

                //show dialog

                dialog.show();

                //init and assign variable
                final EditText editText = dialog.findViewById(R.id.edit_text);
                Button btnUpdate = dialog.findViewById(R.id.button_update);

                //Set text on edit text
                editText.setText(sText);

                btnUpdate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //Dismiss dialog
                        dialog.dismiss();

                        //Get updated text from edit text
                        String uText = editText.getText().toString().trim();

                        //Update text in database
                        database.studentDao().update(sID, uText);

                        //notification when data is updated
                        studentList.clear();
                        studentList.addAll(database.studentDao().getAll());
                        notifyDataSetChanged();
                    }
                });

            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //init student data
                Student s = studentList.get(holder.getAdapterPosition());
                //delete text from db
                database.studentDao().delete(s);

                //notify when data is deleted
                int position = holder.getAdapterPosition();
                studentList.remove(position);
                notifyItemRemoved(position);
                notifyItemChanged(position, studentList.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return studentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView){
            super(itemView);

            textView = itemView.findViewById(R.id.text_view);
            btnDelete = itemView.findViewById(R.id.button_delete);
            btnEdit = itemView.findViewById(R.id.button_eddit);
        }
    }
}

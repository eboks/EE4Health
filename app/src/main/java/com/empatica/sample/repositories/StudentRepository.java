package com.empatica.sample.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.empatica.sample.dao.StudentDao;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.models.Student;

import java.util.List;

public class StudentRepository {

    private StudentDao studentDao;
    private LiveData<List<Student>> allStudents;

    public StudentRepository(Application application){
        RoomDB database = RoomDB.getInstance(application);
        studentDao = database.studentDao();
        allStudents = studentDao.getAllStudents();

    }

    public void insert(Student student){
        new InsertStudentAsyncTask(studentDao).execute(student);

    }

    public void update(Student student){
        new UpdateStudentAsyncTask(studentDao).execute(student);
    }

    public void delete(Student student){
        new DeleteStudentAsyncTask(studentDao).execute(student);

    }

    public void deleteAllStudents(){
        new DeleteAllStudentAsyncTask(studentDao).execute();

    }

    public LiveData<List<Student>> getAllStudents(){
        return allStudents;
    }

    private static class InsertStudentAsyncTask extends AsyncTask<Student, Void, Void>{

        private StudentDao studentDao;

        private InsertStudentAsyncTask(StudentDao studentDao){
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDao.insert(students[0]);
            return null;
        }
    }


    private static class UpdateStudentAsyncTask extends AsyncTask<Student, Void, Void>{

        private StudentDao studentDao;

        private UpdateStudentAsyncTask(StudentDao studentDao){
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDao.update(students[0]);
            return null;
        }
    }
    private static class DeleteStudentAsyncTask extends AsyncTask<Student, Void, Void>{

        private StudentDao studentDao;

        private DeleteStudentAsyncTask(StudentDao studentDao){
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Student... students) {
            studentDao.delete(students[0]);
            return null;
        }
    }

    private static class DeleteAllStudentAsyncTask extends AsyncTask<Void, Void, Void>{

        private StudentDao studentDao;

        private DeleteAllStudentAsyncTask(StudentDao studentDao){
            this.studentDao = studentDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            studentDao.deleteAllStudents();
            return null;
        }
    }
}

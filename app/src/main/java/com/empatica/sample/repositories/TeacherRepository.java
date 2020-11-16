package com.empatica.sample.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.empatica.sample.dao.StudentDao;
import com.empatica.sample.dao.TeacherDao;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.models.Student;
import com.empatica.sample.models.Teacher;

import java.util.List;

public class TeacherRepository {

    private TeacherDao teacherDao;
    private LiveData<List<Teacher>> allTeachers;

    public TeacherRepository(Application application){
        RoomDB database = RoomDB.getInstance(application);
        teacherDao = database.teacherDao();
        allTeachers = teacherDao.getAllTeachers();

    }



    public void insert(Teacher teacher){
        new InsertTeacherAsyncTask(teacherDao).execute(teacher);

    }

    public void update(Teacher teacher){
        new UpdateTeacherAsyncTask(teacherDao).execute(teacher);
    }

    public void delete(Teacher teacher){
        new DeleteTeacherAsyncTask(teacherDao).execute(teacher);

    }

    public void deleteAllTeachers(){
        new DeleteAllTeachersAsyncTask(teacherDao).execute();

    }

    public LiveData<List<Teacher>> getAllTeachers(){
        return allTeachers;
    }

    private static class InsertTeacherAsyncTask extends AsyncTask<Teacher, Void, Void>{

        private TeacherDao teacherDao;

        private InsertTeacherAsyncTask(TeacherDao teacherDao){
            this.teacherDao = teacherDao;
        }

        @Override
        protected Void doInBackground(Teacher... teachers) {
            teacherDao.insert(teachers[0]);
            return null;
        }
    }


    private static class UpdateTeacherAsyncTask extends AsyncTask<Teacher, Void, Void>{

        private TeacherDao teacherDao;

        private UpdateTeacherAsyncTask(TeacherDao teacherDao){
            this.teacherDao = teacherDao;
        }

        @Override
        protected Void doInBackground(Teacher... teachers) {
            teacherDao.update(teachers[0]);
            return null;
        }
    }
    private static class DeleteTeacherAsyncTask extends AsyncTask<Teacher, Void, Void>{

        private TeacherDao teacherDao;

        private DeleteTeacherAsyncTask(TeacherDao teacherDao){
            this.teacherDao = teacherDao;
        }

        @Override
        protected Void doInBackground(Teacher... teachers) {
            teacherDao.delete(teachers[0]);
            return null;
        }
    }

    private static class DeleteAllTeachersAsyncTask extends AsyncTask<Void, Void, Void>{

        private TeacherDao teacherDao;

        private DeleteAllTeachersAsyncTask(TeacherDao teacherDao){
            this.teacherDao = teacherDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            teacherDao.deleteAllTeachers();
            return null;
        }
    }
}

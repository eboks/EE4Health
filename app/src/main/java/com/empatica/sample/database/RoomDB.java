package com.empatica.sample.database;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.empatica.sample.dao.MeasurementDao;
import com.empatica.sample.dao.NoteDao;
import com.empatica.sample.dao.StudentDao;
import com.empatica.sample.dao.TeacherDao;
import com.empatica.sample.models.Measurement;
import com.empatica.sample.models.Note;
import com.empatica.sample.models.Student;
import com.empatica.sample.models.Teacher;


@Database(entities = {Teacher.class, Student.class, Note.class, Measurement.class}, version = 11, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;
    private static String DATABASE_NAME = "database";

    public  synchronized static RoomDB getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return database;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(database).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void>{
        private StudentDao studentDao;

        private PopulateDbAsyncTask(RoomDB db){
            studentDao = db.studentDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            studentDao.insert(new Student("John", "McLaren"));
            studentDao.insert(new Student("Bart", "Meeuws"));
            studentDao.insert(new Student("Kurt", "Dericksen"));
            return null;
        }

    }

    public abstract TeacherDao teacherDao();
    public abstract StudentDao studentDao();
    public abstract NoteDao noteDao();
    public abstract MeasurementDao measurementDao();
}

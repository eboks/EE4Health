package com.empatica.sample.database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.empatica.sample.dao.StudentDao;
import com.empatica.sample.dao.TeacherDao;
import com.empatica.sample.models.Student;
import com.empatica.sample.models.Teacher;


@Database(entities = {Teacher.class, Student.class}, version = 3, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    private static RoomDB database;
    private static String DATABASE_NAME = "database";

    public  synchronized static RoomDB getInstance(Context context){
        if(database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class, DATABASE_NAME)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract TeacherDao teacherDao();
    public abstract StudentDao studentDao();
}

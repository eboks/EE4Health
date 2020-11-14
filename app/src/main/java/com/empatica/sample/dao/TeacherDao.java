package com.empatica.sample.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.empatica.sample.models.Teacher;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TeacherDao {

    @Insert(onConflict = REPLACE)
    void registerTeacher(Teacher teacher);

    @Query("SELECT * FROM teacher WHERE email=(:email) and password=(:password)")
    Teacher login(String email, String password);

    @Delete
    void delete(Teacher teacher);
}

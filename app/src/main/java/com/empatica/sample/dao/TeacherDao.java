package com.empatica.sample.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.empatica.sample.models.Student;
import com.empatica.sample.models.Teacher;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface TeacherDao {

    @Insert(onConflict = REPLACE)
    void insert(Teacher teacher);

    @Query("SELECT * FROM teacher WHERE email=(:email) and password=(:password)")
    Teacher login(String email, String password);

    @Delete
    void delete(Teacher teacher);

    @Delete
    void reset(List<Teacher> teacherList);

    @Update
    void update(Teacher teacher);

    @Query("SELECT * FROM teacher")
    LiveData<List<Teacher>> getAllTeachers();

    @Query("DELETE FROM teacher")
    void deleteAllTeachers();

    @Query("UPDATE teacher SET first_name = :sText WHERE ID = :sID")
    void update(int sID, String sText);
}

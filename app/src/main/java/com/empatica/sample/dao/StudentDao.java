package com.empatica.sample.dao;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.empatica.sample.models.Student;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;


@Dao
public interface StudentDao {
    @Insert(onConflict = REPLACE)
    void insert(Student student);

    @Delete
    void delete(Student student);

    @Delete
    void reset(List<Student> studentList);

    @Update
    void update(Student student);

    @Query("SELECT * FROM student")
    LiveData<List<Student>> getAllStudents();

    @Query("DELETE FROM student")
    void deleteAllStudents();

    @Query("UPDATE student SET first_name = :sText WHERE student_id = :sID")
    void update(int sID, String sText);
}

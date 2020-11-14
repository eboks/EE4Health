package com.empatica.sample.dao;



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
    List<Student> getAll();

    @Query("UPDATE student SET first_name = :sText WHERE ID = :sID")
    void update(int sID, String sText);
}

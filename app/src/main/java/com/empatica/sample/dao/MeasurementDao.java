package com.empatica.sample.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.empatica.sample.models.Measurement;
import com.empatica.sample.models.Note;

import java.util.List;

@Dao
public interface MeasurementDao {
    @Query("SELECT * FROM measurement ORDER BY id DESC")
    List<Measurement> getAllMeasurements();

    @Query("SELECT * FROM measurement WHERE student_id=:studentId")
    List<Measurement> findMeasurementsForStudent(final int studentId);


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);
}

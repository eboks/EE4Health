package com.empatica.sample.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.empatica.sample.models.Note;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface NoteDao {
    @Query("SELECT * FROM note ORDER BY id DESC")
    List<Note> getAllNotes();

    @Query("SELECT * FROM note WHERE student_id=:studentId")
    List<Note> findNotesForStudent(final int studentId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertNote(Note note);

    @Delete
    void deleteNote(Note note);
}

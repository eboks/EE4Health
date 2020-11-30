package com.empatica.sample.repositories;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.empatica.sample.dao.NoteDao;
import com.empatica.sample.dao.StudentDao;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.models.Note;
import com.empatica.sample.models.Student;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>> studentNotes;

    public NoteRepository(Application application){
        RoomDB database = RoomDB.getInstance(application);
        noteDao = database.noteDao();
    }

    public List<Note> findNotesForStudent(int studentId){
        return noteDao.findNotesForStudent(studentId);
    }

    public void insert(Note note){

    }

}

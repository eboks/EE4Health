package com.empatica.sample.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.empatica.sample.models.Note;
import com.empatica.sample.models.Student;
import com.empatica.sample.repositories.NoteRepository;
import com.empatica.sample.repositories.StudentRepository;

import java.util.List;

public class NoteViewModel extends AndroidViewModel {

    private NoteRepository repository;
    private LiveData<List<Note>> allNotes;

    public NoteViewModel(@NonNull Application application) {
        super(application);
        repository = new NoteRepository(application);
        allNotes = repository.getAllNotes();
    }

    public void insert(Note note){
        repository.insert(note);
    }

    public List<Note> getNotesForStudent(int studentId){
        List<Note> studentNotes = repository.findNotesForStudent(studentId);
        return studentNotes;
    }

    public LiveData<List<Note>> getAllNotes(){
        return allNotes;
    }
}

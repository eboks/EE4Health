package com.empatica.sample.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.empatica.sample.dao.NoteDao;
import com.empatica.sample.dao.StudentDao;
import com.empatica.sample.database.RoomDB;
import com.empatica.sample.models.Note;
import com.empatica.sample.models.Student;

import java.util.List;

public class NoteRepository {
    private NoteDao noteDao;
    private LiveData<List<Note>>studentNotes;

    public NoteRepository(Application application){
        RoomDB database = RoomDB.getInstance(application);
        noteDao = database.noteDao();
        studentNotes = noteDao.getAllNotes();
    }

    public List<Note> findNotesForStudent(int studentId){
        return noteDao.findNotesForStudent(studentId);
    }

    public LiveData<List<Note>> getAllNotes(){
        return studentNotes;
    }

    public void insert(Note note){
        new InsertNoteAsyncTask(noteDao).execute(note);

    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insertNote(notes[0]);
            return null;
        }
    }

    public void delete(Note note){
        new DeleteNoteAsyncTask(noteDao).execute(note);

    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteNote(notes[0]);
            return null;
        }
    }

    public void update(Note note){
        new UpdateNoteAsyncTask(noteDao).execute(note);

    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao){
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.updateNote(notes[0]);
            return null;
        }
    }
}

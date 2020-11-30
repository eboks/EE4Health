package com.empatica.sample.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.empatica.sample.models.Student;
import com.empatica.sample.models.Teacher;
import com.empatica.sample.repositories.StudentRepository;
import com.empatica.sample.repositories.TeacherRepository;

import java.util.List;

public class TeacherViewModel extends AndroidViewModel {
    private TeacherRepository repository;
    private LiveData<List<Teacher>> allTeachers;

    public TeacherViewModel(@NonNull Application application) {
        super(application);
        repository = new TeacherRepository(application);
        allTeachers = repository.getAllTeachers();
    }

   // public Teacher login(String username, String password){return repository.login(username, password);}
    public void insert(Teacher teacher){
        repository.insert(teacher);
    }

    public void update(Teacher teacher){
        repository.update(teacher);
    }

    public void delete(Teacher teacher){
        repository.delete(teacher);
    }
    public void deleteAllTeachers(){
        repository.deleteAllTeachers();
    }

    public LiveData<List<Teacher>> getAllTeachers(){
        return allTeachers;
    }


}

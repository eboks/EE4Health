package com.empatica.sample.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "note")

        /*foreignKeys = @ForeignKey(entity = Student.class,
        parentColumns = "student_id",
        childColumns = "note_id",
        onDelete = CASCADE))*/
public class Note implements Serializable {

    //Create id column
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "note_id")
    private int id;


    @ColumnInfo(name="title")
    private String noteTitle;

    @ColumnInfo(name="date_time")
    private String dateTime;

    @ColumnInfo(name = "note_content")
    private String noteContent;

    @ColumnInfo(name="student_id")
    public int studentId;

    public Note(){}

    public Note(String noteTitle, String noteContent, String datetime){
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.dateTime = datetime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public void setNoteTitle(String noteTitle) {
        this.noteTitle = noteTitle;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNoteContent() {
        return noteContent;
    }

    public void setNoteContent(String noteContent) {
        this.noteContent = noteContent;
    }


    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
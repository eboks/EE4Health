package com.empatica.sample.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "note", foreignKeys = @ForeignKey(entity = Student.class,
        parentColumns = "id",
        childColumns = "student_id",
        onDelete = CASCADE))
public class Note implements Serializable {

    //Create id column
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name="title")
    private String firstName;

    @ColumnInfo(name="date_time")
    private String dateTime;

    @ColumnInfo(name = "note_text")
    private String noteText;

    @ColumnInfo(name="student_id")
    public int studentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getNoteText() {
        return noteText;
    }

    public void setNoteText(String noteText) {
        this.noteText = noteText;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }
}
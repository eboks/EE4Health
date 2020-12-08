package com.empatica.sample.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.io.Serializable;

import static androidx.room.ForeignKey.CASCADE;

@Entity(tableName = "measurement",
        foreignKeys = @ForeignKey(entity = Student.class,
        parentColumns = "student_id",
        childColumns = "measurement_id",
        onDelete = CASCADE))
public class Measurement implements Serializable {

    //Create id column
    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "measurement_id")
    private int id;

    @ColumnInfo(name="date_time")
    private String dateTime;

    @ColumnInfo(name = "eda")
    private float eda;

    @ColumnInfo(name = "bvp")
    private float bvp;

    @ColumnInfo(name="student_id")
    public int studentId;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public float getEda() {
        return eda;
    }

    public void setEda(float eda) {
        this.eda = eda;
    }

    public float getBvp() {
        return bvp;
    }

    public void setBvp(float bvp) {
        this.bvp = bvp;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }


}
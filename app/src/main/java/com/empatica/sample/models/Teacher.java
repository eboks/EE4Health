package com.empatica.sample.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "teacher")
public class Teacher implements Serializable {


    //Create id column
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "email")
    private String email;

    @ColumnInfo( name="password")
    private String password;

    @ColumnInfo(name="first_name")
    private String firstName;

    @ColumnInfo(name="last_name")
    private String lastName;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}

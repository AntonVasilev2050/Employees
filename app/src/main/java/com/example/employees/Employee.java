package com.example.employees;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "employees")
public class Employee {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String firstName;
    private String lastName;
    private String birthday;
    private String avatarUrl;
    private int specialtyId;
    private String specialtyName;

    public Employee(int id, String firstName, String lastName, String birthday, String avatarUrl, int specialtyId, String specialtyName) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.avatarUrl = avatarUrl;
        this.specialtyId = specialtyId;
        this.specialtyName = specialtyName;
    }

    @Ignore
    public Employee(String firstName, String lastName, String birthday, String avatarUrl, int specialtyId, String specialtyName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.avatarUrl = avatarUrl;
        this.specialtyId = specialtyId;
        this.specialtyName = specialtyName;
    }

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

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(int specialtyId) {
        this.specialtyId = specialtyId;
    }

    public String getSpecialtyName() {
        return specialtyName;
    }

    public void setSpecialtyName(String specialtyName) {
        this.specialtyName = specialtyName;
    }
}

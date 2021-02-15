package com.example.employees;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Employee.class}, version = 1, exportSchema = false)
public abstract class EmployeesDatabase extends RoomDatabase {
    private static EmployeesDatabase database;
    private static final String DB_NAME = "employees.db";
    private static final Object LOCK = new Object();

    public static EmployeesDatabase getInstance(Context context){
        synchronized (LOCK){
            if(database == null){
                database = Room.databaseBuilder(context, EmployeesDatabase.class, DB_NAME)
                        .build();
            }
        }
        return database;
    }

    public abstract EmployeesDao employeesDao();
}

package com.example.employees;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EmployeesDao {
    @Query("SELECT * FROM employees ORDER BY id")
    LiveData<List<Employee>> getAllEmployees();

    @Insert
    void  insertEmployee(Employee employee);

    @Delete
    void deleteEmployee(Employee employee);

    @Query("DELETE FROM employees")
    void deleteAllNotes();

}

package com.example.employees;

import android.app.Application;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private static EmployeesDatabase database;
    private LiveData<List<Employee>> employees;

    public MainViewModel(@NonNull Application application) {
        super(application);
        database = EmployeesDatabase.getInstance(getApplication());
        employees = database.employeesDao().getAllEmployees();
    }

    public LiveData<List<Employee>> getEmployees() {
        return employees;
    }

    public void insertEmployee(Employee employee){
        new InsertTask().execute(employee);
    }

    private static class InsertTask extends AsyncTask<Employee, Void, Void>{
        @Override
        protected Void doInBackground(Employee... employees) {
            if(employees != null && employees.length > 0){
                database.employeesDao().insertEmployee(employees[0]);
            }
            return null;
        }
    }

    public void deleteEmployee(Employee employee){
        new DeleteTask().execute(employee);
    }

    private static class DeleteTask extends AsyncTask<Employee, Void, Void>{
        @Override
        protected Void doInBackground(Employee... employees) {
            if(employees != null && employees.length > 0){
                database.employeesDao().deleteEmployee(employees[0]);
            }
            return null;
        }
    }

    public void deleteAllEmployees(){
        new DeleteAllTask().execute();
    }

    private static class DeleteAllTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... employees) {
            database.employeesDao().deleteAllNotes();
            return null;
        }
    }
}

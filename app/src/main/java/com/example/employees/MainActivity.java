package com.example.employees;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    private static final String url = "https://gitlab.65apps.com/65gb/static/raw/master/testTask.json";
    ArrayList<Employee> employees;
    ArrayList<Employee> employeesSpecialty;
    private EmployeesShortInfoAdapter shortInfoAdapter;
    private MainViewModel viewModel;

    private ListView listViewSpecialties;
    private RecyclerView recyclerViewSpecialty;
    private int chosenSpecialty;
    private TextView textViewEmployeeListLabel;
    private TextView textViewFirstName;
    private TextView textViewLastName;
    private TextView textViewBirthday;
    private TextView textViewAge;
    private TextView textViewAgeShort;
    private TextView textViewSpecialtyName;
    private TextView textViewSpecialtyName2;
    private ImageView imageViewAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
        listViewSpecialties = findViewById(R.id.listViewSpecialties);
        recyclerViewSpecialty = findViewById(R.id.recyclerViewSpecialty);
        textViewEmployeeListLabel = findViewById(R.id.textViewEmployeeListLabel);
        textViewFirstName = findViewById(R.id.textViewFirstName);
        textViewLastName = findViewById(R.id.textViewLastName);
        textViewBirthday = findViewById(R.id.textViewBirthday);
        textViewAge = findViewById(R.id.textViewAge);
        textViewAgeShort = findViewById(R.id.textViewAgeShort);
        textViewSpecialtyName = findViewById(R.id.textViewSpecialtyName);
        textViewSpecialtyName2 = findViewById(R.id.textViewSpecialtyName2);
        imageViewAvatar = findViewById(R.id.imageViewAvatar);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);
//        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        employeesSpecialty = new ArrayList<>();
        employees = getEmployees(url);
        getData();
        makeCorrect(employees);
        showEmployees(employees);
        listViewSpecialties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                employeesSpecialty.clear();
                switch (position) {
                    case 0:
                        chosenSpecialty = 101;
                        for (int i = 0; i < employees.size(); i++){
                            if(employees.get(i).getSpecialtyId() == chosenSpecialty){
                                employeesSpecialty.add(employees.get(i));
                            }
                        }
                        textViewEmployeeListLabel.setText(R.string.manager_list);
                        showEmployees(employeesSpecialty);
                        break;
                    case 1:
                        chosenSpecialty = 102;
                        for(Employee employee: employees){
                            if(employee.getSpecialtyId() == chosenSpecialty){
                                employeesSpecialty.add(employee);
                            }
                        }
                        textViewEmployeeListLabel.setText(R.string.developer_list);
                        showEmployees(employeesSpecialty);
                        break;
                    case 2:
                        textViewEmployeeListLabel.setText(R.string.all_specialties_list);
                        showEmployees(employees);
                        break;
                }
            }
        });
    }

    private void showEmployees(ArrayList<Employee> employees) {
        shortInfoAdapter = new EmployeesShortInfoAdapter(employees);
        recyclerViewSpecialty.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerViewSpecialty.setAdapter(shortInfoAdapter);
        shortInfoAdapter.setOnEmployeeClickListener(new EmployeesShortInfoAdapter.OnEmployeeClickListener() {
            @Override
            public void onEmployeeClick(int position) {
                textViewFirstName.setText(shortInfoAdapter.employees.get(position).getFirstName());
                textViewLastName.setText(shortInfoAdapter.employees.get(position).getLastName());
                if(!shortInfoAdapter.employees
                        .get(position)
                        .getBirthday().equals("null")
                        && !shortInfoAdapter.employees
                        .get(position).getBirthday().equals("") ){
                    textViewBirthday.setText(shortInfoAdapter.employees.get(position).getBirthday());
                }else {
                    textViewBirthday.setText("нет данных");
                }

                String employeeBirthday = employees.get(position).getBirthday();
                SimpleDateFormat formatter;
                if (employeeBirthday.length() > 4) {
                    if (employeeBirthday.charAt(4) == '-') {
                        formatter = new SimpleDateFormat("YYYY-MM-dd", Locale.ENGLISH);
                    } else {
                        formatter = new SimpleDateFormat("dd-MM-YYYY", Locale.ENGLISH);
                    }
                    try {
                        Date employeeBirthdayDate = formatter.parse(employeeBirthday);
                        Date today = new Date();

                        long age = ((today.getTime() - employeeBirthdayDate.getTime()) / 31536000000L);
                        textViewAge.setText(Long.toString(age));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    textViewAge.setText("нет данных");
                }

                textViewSpecialtyName.setText(shortInfoAdapter.employees.get(position).getSpecialtyName());
                String avatarUrl = shortInfoAdapter.employees.get(position).getAvatarUrl();
                DownloadImageTask task = new DownloadImageTask();
                Bitmap bitmap = null;
                try {
                    bitmap = task.execute(avatarUrl).get();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if(bitmap != null) {
                    imageViewAvatar.setImageBitmap(bitmap);
                }else {
                    imageViewAvatar.setImageResource(R.drawable.face);
                }
            }
        });
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                return BitmapFactory.decodeStream(in);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
            }
            return null;
        }
    }

    private ArrayList<Employee> getEmployees(String url) {
        ArrayList<Employee> employees = new ArrayList<>();
        String employeesJSON = null;
        DownloadTask task = new DownloadTask();
        try {
            employeesJSON = task.execute(url).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        if (employeesJSON == null) {
            return employees;
        }
        try {
            JSONObject jsonObject = new JSONObject(employeesJSON);
            JSONArray response = jsonObject.getJSONArray("response");
            viewModel.deleteAllEmployees();
            for (int i = 0; i < response.length(); i++) {
                JSONObject jsonEmployee = response.getJSONObject(i);
                int id = i;
                String fistName = jsonEmployee.getString("f_name");
                String lastName = jsonEmployee.getString("l_name");
                String birthday = jsonEmployee.getString("birthday");
                String avatarUrl = jsonEmployee.getString("avatr_url");
                JSONArray specialty = jsonEmployee.getJSONArray("specialty");
                JSONObject sp0 = specialty.getJSONObject(0);
                int specialtyId = sp0.getInt("specialty_id");
                String specialtyName = sp0.getString("name");
                Employee employee = new Employee(fistName, lastName, birthday, avatarUrl, specialtyId, specialtyName);
                employees.add(employee);
                viewModel.insertEmployee(employee);
            }
            return employees;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return employees;
    }

    private static class DownloadTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            StringBuilder result = new StringBuilder();
            URL url = null;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                BufferedReader bufferedReader = new BufferedReader(reader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    result.append(line);
                    line = bufferedReader.readLine();
                }
                return result.toString();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }

    private void makeCorrect(ArrayList<Employee> employees){
        for(Employee employee: employees){
            employee.setFirstName(employee.getFirstName().toUpperCase());
            employee.setLastName(employee.getLastName().toUpperCase());
        }
    }

    private void getData() {
        LiveData<List<Employee>> employeesFromDB = viewModel.getEmployees();
        employeesFromDB.observe(this, new Observer<List<Employee>>() {
            @Override
            public void onChanged(List<Employee> employeesFromLiveData) {
                employees.clear();
                employees.addAll(employeesFromLiveData);
                shortInfoAdapter.setEmployees(employees);
            }
        });
    }
}
package com.example.employees;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class EmployeesShortInfoAdapter extends RecyclerView.Adapter<EmployeesShortInfoAdapter.EmployeeViewHolder> {
    List<Employee> employees;

    public EmployeesShortInfoAdapter(List<Employee> employees) {
        this.employees = employees;
    }

    private OnEmployeeClickListener onEmployeeClickListener;

    interface OnEmployeeClickListener {
        void onEmployeeClick(int position);
    }

    public void setOnEmployeeClickListener(OnEmployeeClickListener onEmployeeClickListener) {
        this.onEmployeeClickListener = onEmployeeClickListener;
    }

    @NonNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.employee_short_info_item, parent, false);
        return new EmployeeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = employees.get(position);
        holder.textViewFirstNameShort.setText(employee.getFirstName());
        holder.textViewLastNameShort.setText(employee.getLastName());
        String employeeBirthday = employee.getBirthday();
        SimpleDateFormat formatter;
        if (employeeBirthday.length() > 4) {
            if (employeeBirthday.charAt(4) == '-') {
                formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
            } else {
                formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
            }
            try {
                Date employeeBirthdayDate = formatter.parse(employeeBirthday);
                Date today = new Date();
                long age = ((today.getTime() - employeeBirthdayDate.getTime()) / 31536000000L);
                holder.textViewAgeShort.setText(Long.toString(age));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            holder.textViewAgeShort.setText("нет данных");
        }

        holder.textViewSpecialtyName2.setText(employee.getSpecialtyName());
        holder.textViewAvatarUrl.setText(employee.getAvatarUrl());
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    class EmployeeViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewFirstNameShort;
        private TextView textViewLastNameShort;
        private TextView textViewAgeShort;
        private TextView textViewSpecialtyName2;
        private TextView textViewAvatarUrl;

        public EmployeeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewFirstNameShort = itemView.findViewById(R.id.textViewFirstNameShort);
            textViewLastNameShort = itemView.findViewById(R.id.textViewLastNameShort);
            textViewAgeShort = itemView.findViewById(R.id.textViewAgeShort);
            textViewSpecialtyName2 = itemView.findViewById(R.id.textViewSpecialtyName2);
            textViewAvatarUrl = itemView.findViewById(R.id.textViewAvatarUrl);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (onEmployeeClickListener != null) {
                        onEmployeeClickListener.onEmployeeClick(getAdapterPosition());
//                        textViewFirstNameShort.setBackgroundColor(itemView.getResources().getColor(R.color.material_on_background_emphasis_medium));
//                        textViewLastNameShort.setBackgroundColor(itemView.getResources().getColor(R.color.material_on_background_emphasis_medium));
                    }
                }
            });
        }
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
        notifyDataSetChanged();
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}

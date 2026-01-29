package com.example.budgetwolt_android_final.activities;

import static com.example.budgetwolt_android_final.utilities.Constants.INSERT_DRIVER_URL;
import static com.example.budgetwolt_android_final.utilities.Constants.INSERT_USER_URL;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.utilities.RestOperations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.Calendar;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etPassword, etName, etSurname, etPhone, etAddress, etLicense, etDob;
    private RadioGroup rgRole;
    private LinearLayout layoutDriverFields;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = findViewById(R.id.etRegUsername);
        etPassword = findViewById(R.id.etRegPassword);
        etName = findViewById(R.id.etRegName);
        etSurname = findViewById(R.id.etRegSurname);
        etPhone = findViewById(R.id.etRegPhone);
        etAddress = findViewById(R.id.etRegAddress);
        etLicense = findViewById(R.id.etRegLicense);
        etDob = findViewById(R.id.etRegDob);
        rgRole = findViewById(R.id.rgRole);
        layoutDriverFields = findViewById(R.id.layoutDriverFields);
        btnSubmit = findViewById(R.id.btnRegisterSubmit);

        rgRole.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rbDriver) {
                layoutDriverFields.setVisibility(View.VISIBLE);
            } else {
                layoutDriverFields.setVisibility(View.GONE);
            }
        });

        etDob.setOnClickListener(v -> showDatePicker());

        btnSubmit.setOnClickListener(v -> submitForm());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR) - 18;
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    String date = year1 + "-" + String.format("%02d", (monthOfYear + 1)) + "-" + String.format("%02d", dayOfMonth);
                    etDob.setText(date);
                }, year, month, day);
        datePickerDialog.show();
    }

    private void submitForm() {
        boolean isDriver = rgRole.getCheckedRadioButtonId() == R.id.rbDriver;

        JsonObject json = new JsonObject();
        json.addProperty("username", etUsername.getText().toString());
        json.addProperty("password", etPassword.getText().toString());
        json.addProperty("name", etName.getText().toString());
        json.addProperty("surname", etSurname.getText().toString());
        json.addProperty("phoneNumber", etPhone.getText().toString());
        json.addProperty("address", etAddress.getText().toString());

        String url;
        if (isDriver) {
            json.addProperty("driverLicense", etLicense.getText().toString());
            json.addProperty("dateOfBirth", etDob.getText().toString());
            url = INSERT_DRIVER_URL;
        } else {
            url = INSERT_USER_URL;
        }

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.post(url, new Gson().toJson(json));
                handler.post(() -> {
                    if (!response.equals("ERROR")) {
                        Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(this, "Registration failed. Username might be taken.", Toast.LENGTH_SHORT).show();
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
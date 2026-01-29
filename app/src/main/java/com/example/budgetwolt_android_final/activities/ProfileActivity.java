package com.example.budgetwolt_android_final.activities;

import static com.example.budgetwolt_android_final.utilities.Constants.UPDATE_USER_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.models.BasicUser;
import com.example.budgetwolt_android_final.models.Driver;
import com.example.budgetwolt_android_final.models.User;
import com.example.budgetwolt_android_final.utilities.LocalDateAdapter;
import com.example.budgetwolt_android_final.utilities.RestOperations;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ProfileActivity extends AppCompatActivity {

    private User currentUser;
    private EditText etUsername, etPassword, etName, etSurname, etAddress, etPhoneNumber, etDriverLicense, etDob;

    private Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String userInfo = intent.getStringExtra("userJson");

        Log.d("INFO", "USER: " + userInfo);

        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .create();
        currentUser = gson.fromJson(userInfo, User.class);

        etUsername = findViewById(R.id.etUsername);
        etUsername.setEnabled(false);
        etPassword = findViewById(R.id.etPassword);
        etName = findViewById(R.id.etName);
        etSurname = findViewById(R.id.etSurname);
        etAddress = findViewById(R.id.etAddress);
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etDriverLicense = findViewById(R.id.etDriverLicense);
        etDob = findViewById(R.id.etDob);

        if (currentUser instanceof Driver) {
            currentUser = gson.fromJson(userInfo, Driver.class);
        } else {
            currentUser = gson.fromJson(userInfo, BasicUser.class);
            etDriverLicense.setVisibility(View.GONE);
            etDob.setVisibility(View.GONE);
        }

        fillFields();

    }

    private void fillFields() {
        etUsername.setText(currentUser.getUsername());
        etPassword.setText(currentUser.getPassword());
        etName.setText(currentUser.getName());
        etSurname.setText(currentUser.getSurname());
        etPhoneNumber.setText(currentUser.getPhoneNumber());

        if (currentUser instanceof BasicUser) {
            etAddress.setText(((BasicUser) currentUser).getAddress());
        }
        if (currentUser instanceof Driver) {
            etDriverLicense.setText(((Driver) currentUser).getDriverLicense());

            LocalDate dob = ((Driver) currentUser).getDateOfBirth();
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String dateString = dob.format(dtf);

            etDob.setText(dateString);
        }
    }

    public void saveUserProfile(View view) {
        User userToUpdate;

        String username = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        String name = etName.getText().toString();
        String surname = etSurname.getText().toString();
        String address = etAddress.getText().toString();
        String phone = etPhoneNumber.getText().toString();


        if (currentUser instanceof Driver) {
            Driver driver = new Driver();

            String driverLicense = etDriverLicense.getText().toString();
            String dateOfBirthString = etDob.getText().toString();

            if (driverLicense.isEmpty() || dateOfBirthString.isEmpty()) {
                Toast.makeText(this, "License and DOB are required for drivers.", Toast.LENGTH_SHORT).show();
                return;
            }

            driver.setDriverLicense(driverLicense);
            driver.setAddress(address);

            try {
                LocalDate dob = LocalDate.parse(dateOfBirthString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                driver.setDateOfBirth(dob);
            } catch (Exception e) {
                Toast.makeText(this, "Invalid Date Format (use YYYY-MM-DD).", Toast.LENGTH_SHORT).show();
                return;
            }

            userToUpdate = driver;
        } else if (currentUser instanceof BasicUser){
            BasicUser basicUser = new BasicUser();

            basicUser.setAddress(address);

            userToUpdate = basicUser;
        } else {
            userToUpdate = null;
        }

        if (userToUpdate == null) {
            return;
        }

        userToUpdate.setId(currentUser.getId());
        userToUpdate.setUsername(username);
        userToUpdate.setPassword(password);
        userToUpdate.setName(name);
        userToUpdate.setSurname(surname);
        userToUpdate.setPhoneNumber(phone);
        userToUpdate.setDateCreated(currentUser.getDateCreated());
        userToUpdate.setDateModified(LocalDate.now());

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        Log.d("INFO", "Update Json: " + gson.toJson(userToUpdate));

        executor.execute(() -> {
            try {
                String response = RestOperations.put(UPDATE_USER_URL, gson.toJson(userToUpdate));

                handler.post(() -> {
                    if (!response.equals("ERROR") && !response.isEmpty()) {

                        Log.d("INFO", "PUT RESPONSE: " + response);

                        Intent intent = new Intent(ProfileActivity.this, ProfileActivity.class);
                        intent.putExtra("userJson", response);
                        Log.d("myINFO", response);
                        startActivity(intent);

                    } else {
                        Toast.makeText(ProfileActivity.this, "Update Unsuccessful", Toast.LENGTH_LONG).show();
                    }
                });


            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });


    }
}
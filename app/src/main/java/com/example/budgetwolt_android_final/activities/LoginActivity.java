package com.example.budgetwolt_android_final.activities;

import static com.example.budgetwolt_android_final.utilities.Constants.VALIDATE_LOGIN_URL;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.models.Driver;
import com.example.budgetwolt_android_final.utilities.RestOperations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void validateLogin(View view) {
        TextView usernameField = findViewById(R.id.editTextUsername);
        TextView passwordField = findViewById(R.id.editTextPassword);

        Gson gson = new Gson();

        JsonObject data = new JsonObject();
        data.addProperty("username", usernameField.getText().toString());
        data.addProperty("password", passwordField.getText().toString());

        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String response = RestOperations.post(VALIDATE_LOGIN_URL, gson.toJson(data));

                handler.post(() -> {
                    if (!response.equals("ERROR") && !response.isEmpty()) {
                        Log.d("SUCCESS", "Validate Login - OK 200");

                        JsonObject userObject = gson.fromJson(response, JsonObject.class);

                        Intent intent;

                        Log.d("ORD_HISTORY", "validateLogin: " + userObject);
                        Log.d("INFO", "validateLogin: " + response);

                        if (userObject.get("userType").toString().contains("Driver")) {
                            Log.d("LOGIN_TYPE", "User is a Driver");
                            intent = new Intent(LoginActivity.this, DriverMainActivity.class);
                        } else if (userObject.get("userType").toString().contains("Restaurant")) {
                            Log.d("LOGIN", "validateLogin: BUT USER IS A RESTAURANT THO");
                            Toast.makeText(LoginActivity.this, "Invalid User. Admins/Restaurants must use desktop app", Toast.LENGTH_LONG).show();
                            return;
                        } else if (userObject.get("userType").toString().contains("BasicUser")) {
                                Log.d("LOGIN_TYPE", "User is a Client");
                                intent = new Intent(LoginActivity.this, MainActivity.class);
                        } else {
                            Toast.makeText(LoginActivity.this, "Invalid User. Admins/Restaurants must use desktop app", Toast.LENGTH_LONG).show();
                            return;
                        }


                        intent.putExtra("userJson", response);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_LONG).show();
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void openRegisterActivity(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }
}
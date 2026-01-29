package com.example.budgetwolt_android_final.activities;

import static com.example.budgetwolt_android_final.utilities.Constants.CHAT_BY_ORDER_URL;
import static com.example.budgetwolt_android_final.utilities.Constants.SEND_MESSAGE_URL;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.budgetwolt_android_final.R;
import com.example.budgetwolt_android_final.models.Review;
import com.example.budgetwolt_android_final.utilities.MessageDTO;
import com.example.budgetwolt_android_final.utilities.RestOperations;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class ChatActivity extends AppCompatActivity {
    private ListView lvMessages;
    private EditText etMessage;
    private Button btnSend;
    private int orderId;
    private boolean isReadOnly;

    private int currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        lvMessages = findViewById(R.id.lvMessages);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        currentUserId = getIntent().getIntExtra("currentUserId", 0);
        orderId = getIntent().getIntExtra("orderId", -1);
        isReadOnly = getIntent().getBooleanExtra("isReadOnly", false);

        if (isReadOnly) {
            etMessage.setEnabled(false);
            etMessage.setHint("Order completed - View Only");
            btnSend.setVisibility(View.GONE);
        }

        btnSend.setOnClickListener(v -> {
            String text = etMessage.getText().toString();
            if (!text.isEmpty()) {
                sendMessage(text);
            }
        });

        loadChatHistory();
    }

    private void loadChatHistory() {
        Executor executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(() -> {
            try {
                String url = CHAT_BY_ORDER_URL + "/" + orderId;
                String response = RestOperations.get(url);

                handler.post(() -> {
                    if (response != null && !response.equals("ERROR")) {
                        Log.d("INFO_MSG", "loadChatHistory: " + response);
                        Type listType = new TypeToken<List<MessageDTO>>(){}.getType();
                        List<MessageDTO> messages = new Gson().fromJson(response, listType);

                        if (messages != null) {
                            updateUI(messages);
                        }
                    }
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateUI(List<MessageDTO> messages) {
        List<String> displayMessages = new ArrayList<>();

        for (MessageDTO msg : messages) {
            displayMessages.add(msg.sender + ": " + msg.text);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                displayMessages
        );

        lvMessages.setAdapter(adapter);
        lvMessages.setSelection(adapter.getCount() - 1);
    }

    private void sendMessage(String text) {
        JsonObject data = new JsonObject();
        data.addProperty("messageText", text);
        data.addProperty("userId", currentUserId);
        data.addProperty("orderId", orderId);

        Log.d("INFO_MSG", "sendMessage: " + text + " " + currentUserId + " " + orderId);

        Executors.newSingleThreadExecutor().execute(() -> {
            try {
                RestOperations.post(SEND_MESSAGE_URL, new Gson().toJson(data));
                runOnUiThread(() -> {
                    etMessage.setText("");
                    loadChatHistory();
                });
            } catch (IOException e) { e.printStackTrace(); }
        });
    }
}
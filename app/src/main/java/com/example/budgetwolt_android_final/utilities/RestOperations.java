package com.example.budgetwolt_android_final.utilities;

import android.util.Log;

import androidx.annotation.NonNull;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class RestOperations {

    public static String get(String url) throws IOException {

        HttpURLConnection httpURLConnection = setupHTTPConnection(url, "GET");

        return retrieveData(httpURLConnection);
    }

    public static String delete(String url) throws IOException {
        HttpURLConnection httpURLConnection = setupHTTPConnection(url, "DELETE");

        return retrieveData(httpURLConnection);
    }

    public static String post(String url, String dataParams) throws IOException {

        HttpURLConnection httpURLConnection = setupHTTPConnection(url, "POST");

        sendParamData(httpURLConnection, dataParams);

        return retrieveData(httpURLConnection);
    }

    public static String put(String url, String dataParams) throws IOException {

        HttpURLConnection httpURLConnection = setupHTTPConnection(url, "PUT");

        sendParamData(httpURLConnection, dataParams);

        return retrieveData(httpURLConnection);
    }

    private static HttpURLConnection setupHTTPConnection(String requestURL, @NonNull String requestMethod) throws IOException {
        URL url = new URL(requestURL);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        httpURLConnection.setRequestMethod(requestMethod);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

        if (requestMethod.equals("POST") || requestMethod.equals("PUT")) {
            httpURLConnection.setReadTimeout(15000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
        }

        return httpURLConnection;
    }

    private static void sendParamData(HttpURLConnection httpURLConnection, String paramData) throws IOException {
        OutputStream outputStream = httpURLConnection.getOutputStream();
        BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, StandardCharsets.UTF_8));
        bufferedWriter.write(paramData);
        bufferedWriter.close();
        outputStream.close();
    }

    private static String retrieveData(HttpURLConnection httpURLConnection) throws IOException {
        int responseCode = httpURLConnection.getResponseCode();
        Log.d("INFO", "Response Code: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            StringBuilder response = new StringBuilder();
            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();
            return response.toString();
        } else {
            return "ERROR";
        }
    }
}
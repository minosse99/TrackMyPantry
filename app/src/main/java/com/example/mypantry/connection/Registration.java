package com.example.mypantry.connection;

import android.os.AsyncTask;
import android.util.Log;

import com.example.mypantry.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Registration extends AsyncTask<String, Void, String> {

    private static final OkHttpClient client = new OkHttpClient();

    @Override
    protected String doInBackground(String... strings) {
        final int[] result = {0};

        RequestBody formBody = new FormBody.Builder()
                .add("username", strings[0])
                .add("email",strings[1])
                .add("password",strings[2])
                .build();

        Request request = new Request.Builder()
                .url(AuthRequest.url+"users")
                .post(formBody)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            else{
            String res = response.body().string();
            JSONObject object = null;
            try {
                object = (JSONObject) new JSONObject(res);
                String id = object.getString("id");
                AuthRequest.login(strings[1],strings[2]);
                return id;
            }catch(JSONException e){e.printStackTrace();}}
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

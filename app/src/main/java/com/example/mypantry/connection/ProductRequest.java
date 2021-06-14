package com.example.mypantry.connection;

import android.os.AsyncTask;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.BufferedSink;

public class ProductRequest extends AsyncTask<String, Void, String> {
    private static final OkHttpClient client = new OkHttpClient();
    public static String url = AuthRequest.url;
    public static String products = "products";
    public static String tokenSession;

    @Override
    public String doInBackground(String... strings) {

        Request request = new Request.Builder()
                .url(url + products + "?" + "barcode=" + strings[0])
                .header("Authorization", "Bearer " + AuthToken.getToken())
                .build();


        try {
            try (Response response = client.newCall(request).execute()) {
                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
                    String res = response.body().string();
                    JSONObject object = null;
                    try {
                        object = (JSONObject) new JSONObject(res);
                        String token = object.getString("token");
                        JSONArray product = (JSONArray) object.get("products");
                        tokenSession = token;
                        Log.e("token", token);
                        Log.e("Array", product.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }



            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "ciao";
    }



}


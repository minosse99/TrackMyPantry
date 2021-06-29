package com.example.mypantry.connection;

import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import com.example.mypantry.activity.ActivitySearch;
import com.example.mypantry.ui.login.LoginActivity;

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

public class ProductRequest {
    private static final OkHttpClient client = new OkHttpClient();
    public static String url = AuthRequest.url;
    public static String products = "products";
    public static String tokenSession;


    public static void requestList(String barcode) {

        Request request = new Request.Builder()
                .url(url + products + "?" + "barcode=" + barcode)
                .header("Authorization", "Bearer " + AuthToken.getToken())
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String res = response.body().string();
                JSONObject object = null;
                try {
                    object = (JSONObject) new JSONObject(res);
                    String token = object.getString("token");
                    JSONArray product = (JSONArray) object.get("products");
                    ActivitySearch.listProduct = product;
                    tokenSession = token;
                    Log.e("token", token);
                    Log.e("Array", product.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }







}



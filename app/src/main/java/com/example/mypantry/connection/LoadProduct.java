package com.example.mypantry.connection;

import android.provider.MediaStore;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoadProduct {
    private static final OkHttpClient client = new OkHttpClient();


    public static void addProduct(String name, String describes, String barcode) throws JSONException {
        RequestBody formBody = new FormBody.Builder()
                .add("token",ProductRequest.tokenSession)
                .add("name",name)
                .add("description",describes)
                .add("barcode",barcode)
                .add("test", String.valueOf(Boolean.TRUE))
                .build();
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        JSONObject json = new JSONObject();
        json.put("token",ProductRequest.tokenSession);
        json.put("name",name);
        json.put("desciption",describes);
        json.put("barcode",barcode);
        json.put("test",true);
        Log.e("JSON",json.toString());
        RequestBody rb = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder()
                .post(formBody)
                .url(ProductRequest.url + ProductRequest.products)
                .header("Authorization", "Bearer " + AuthToken.getToken())
                .build();

        Log.d("Request",request.toString());
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.e("RES",response.body().string());
            }
        });
    }
}

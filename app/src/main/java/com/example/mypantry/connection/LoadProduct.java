package com.example.mypantry.connection;

import android.provider.MediaStore;
import android.util.Log;

import com.example.mypantry.Utils;
import com.example.mypantry.ui.home.HomeFragment;

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
import okhttp3.internal.Util;
import okio.Buffer;

public class LoadProduct {
    private static final OkHttpClient client = new OkHttpClient();


    public static void addProduct(String name, String describes, String barcode) throws JSONException {
     /*   RequestBody formBody = new FormBody.Builder()
                .add("token",ProductRequest.tokenSession)
                .add("name",name)
                .add("description",describes)
                .add("barcode",barcode)
                .add("test", String.valueOf(Boolean.TRUE))
                .build();
       */
        MediaType JSON = MediaType.parse("application/json");
        JSONObject json = new JSONObject();
        json.put("token",ProductRequest.tokenSession);
        json.put("name",name);
        json.put("description",describes);
        json.put("barcode",barcode);
        json.put("test",Boolean.valueOf(true));

        RequestBody rb = RequestBody.create(JSON, String.valueOf(json));

        Request request = new Request.Builder()
                .url(ProductRequest.url + ProductRequest.products)
                .header("Authorization", "Bearer " + AuthToken.getToken())
                .post(rb)
                .build();


        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {

            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.e("RES",response.body().string());


                //HomeFragment.db.save(barcode,name,describes);
            }
        });
    }


}

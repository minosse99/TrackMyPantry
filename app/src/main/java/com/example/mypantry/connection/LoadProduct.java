package com.example.mypantry.connection;

import android.util.Log;

import com.example.mypantry.Utils;
import com.example.mypantry.data.ui.fragment.HomeFragment;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoadProduct {
    private static final OkHttpClient client = new OkHttpClient();


    public static void addProduct(String name, String describes, String barcode) throws JSONException {

        MediaType JSON = MediaType.parse("application/json");
        JSONObject json = new JSONObject();
        json.put("token",ProductRequest.tokenSession);
        json.put("name",name);
        json.put("description",describes);
        json.put("barcode",barcode);
        json.put("test",Boolean.valueOf(false));

        RequestBody rb = RequestBody.create(JSON, String.valueOf(json));

        Request request = new Request.Builder()
                .url(ProductRequest.url + ProductRequest.products)
                .header("Authorization", "Bearer " + AuthToken.getToken())
                .post(rb)
                .build();

        Log.e("PROVA","PROVA");
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("FAL","onFailure");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
          //      Log.e("RES",response.body().string());
                String res = response.body().string();
                try {
                    JSONObject obj = new JSONObject(res);
                    String name =  Utils.getData(obj, "name");
                    String description = Utils.getData(obj, "description");
                    String barcode = Utils.getData(obj, "barcode");
                    String id = Utils.getData(obj,"id");

                    Log.e("RES",response.toString());
                    HomeFragment.db.save( name,barcode, description,1,id);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}

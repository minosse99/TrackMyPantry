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
    protected static String url = AuthRequest.url;
    protected static String products = "products";
    protected static String tokenSession;

    @Override
    public String doInBackground(String... strings) {

        Request request = new Request.Builder()
                .url(url + products + "?" + "barcode=" + Arrays.toString(strings))
                .header("Authorization", "Bearer " + AuthToken.getToken())
                .build();

        Log.e("URL", url + products + "?" + "barcode=" + Arrays.toString(strings));
        Log.e("SCHEMA", request.toString());


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


    public static void addProduct(String name, String describes, String barcode) throws JSONException {
        RequestBody formBody = new FormBody.Builder()
                .add("token",tokenSession)
                .add("name",name)
                .add("description",describes)
                .add("barcode",barcode)
                .add("test", String.valueOf(Boolean.TRUE))
                .build();


        JSONObject json = new JSONObject();
            json.put("token",tokenSession);
            json.put("name",name);
            json.put("desciption",describes);
            json.put("barcode",barcode);
            json.put("test",true);

        final MediaType JSON
                = MediaType.parse("application/json; charset=utf-8");


        Log.e("JSON",json.toString());
        RequestBody rb = RequestBody.create(JSON, json.toString());

        Request request = new Request.Builder()
                .post(rb)
                .url(url + products)
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



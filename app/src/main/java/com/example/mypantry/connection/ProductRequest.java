package com.example.mypantry.connection;

import android.util.Log;

import com.example.mypantry.R;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ProductRequest extends AuthToken{
        private static final OkHttpClient client = new OkHttpClient();
        protected static String url = AuthRequest.url;
        protected static String products = "products";

        public static void productsList(String barcode) {
            Log.e("Product Request","inline");
            Log.e("URL", url + products + "?" + "barcode=" + barcode);

            Request request = new Request.Builder()
                    .url(url + products + "?" + "barcode=" + barcode)
                    .header("Authorization","Bearer " + AuthToken.getToken())
                    .build();

            Log.e("URL", url + products + "?" + "barcode=" + barcode);
            Log.e("SCHEMA", request.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e("Errore", e.toString());
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String res = response.body().string();
                    JSONObject object = null;
                    try {
                        object = (JSONObject) new JSONObject(res);
                        String token = object.getString("token");
                        JSONArray product = (JSONArray) object.get("products");
                        Log.e("token",token);
                        Log.e("Array",product.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });

        }

    }


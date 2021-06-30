package com.example.mypantry.connection;

import android.util.Log;

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

public class VoteProduct {
    private static final OkHttpClient client = new OkHttpClient();
    private static final String votes = "votes";

    public static void voteProduct(String id) throws JSONException {
        MediaType JSON = MediaType.parse("application/json");
        JSONObject json = new JSONObject();
        json.put("token",ProductRequest.tokenSession);
        json.put("rating",1);
        json.put("productId",id);
        RequestBody rb = RequestBody.create(JSON, String.valueOf(json));

        Request request = new Request.Builder()
                .url(ProductRequest.url + votes)
                .header("Authorization", "Bearer " + AuthToken.getToken())
                .post(rb)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.d("Vote Product Response",response.body().string());
            }
        });
    }
}

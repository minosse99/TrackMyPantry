package com.example.mypantry.connection;

import android.content.Intent;
import android.util.Log;

import com.example.mypantry.Utils;
import com.example.mypantry.data.model.LoggedInUser;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class AuthRequest {
    public static final String url = "https://lam21.modron.network/";
    private static final OkHttpClient client = new OkHttpClient();
    private static String tokenSession = null;

/*
    public static void register(String username,String email, String password){
        final int[] result = {0};
        RequestBody formBody = new FormBody.Builder()
                .add("username",username)
                .add("email",email)
                .add("password",password)
                .build();

        Request request = new Request.Builder()
                .url(url+"users")
                .post(formBody)
                .build();
    client.newCall(request).enqueue(new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            Log.d("Register",response.body().string());
        }
        });
    }
*/

    public static LoggedInUser login(String email, String password){
       RequestBody formBody = new FormBody.Builder()
                .add("email",email)
                .add("password",password)
                .build();

        Log.i("body",formBody.toString());
        Request request = new Request.Builder()
                .url(url+"auth/login")
                .post(formBody)
                .build();

        Log.e("RequestBody", Utils.bodyToString(request));
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure( Call call, IOException e) { e.printStackTrace();}

            @Override
            public void onResponse( Call call, Response response) throws IOException {
                String res = response.body().string();
                Log.e("response login",res);
                try {
                    if(!(response.code() > 400)) {

                        JSONObject object = (JSONObject) new JSONTokener(res).nextValue();
                        String token = object.getString("accessToken");
                        Log.d("ACCESS TOKEN", token);
                        AuthToken.assign(token, email);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();

                }

            }
        });

        return new LoggedInUser(
                java.util.UUID.randomUUID().toString(),
                email);
    }

}


//simone@gmail.com
//simone
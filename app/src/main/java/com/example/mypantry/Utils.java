package com.example.mypantry;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.example.mypantry.ui.login.LoginActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Request;
import okio.Buffer;

public class Utils {
    public static String bodyToString(final Request request){

        try {
            final Request copy = request.newBuilder().build();
            final Buffer buffer = new Buffer();
            copy.body().writeTo(buffer);
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    public static String[] getCharSequence(JSONArray list) throws JSONException {

        Log.d("prova","prova");
        String[] appo = new String[20];
        int count = 0;
        for(int i = 0 ; i< list.length();i++){
            JSONObject obj = getJSONObject(list.get(i));
            if(!obj.getBoolean("test") && count < 20) {
                appo[count] = obj.getString("name");
                count++;
            }
        }
        String[] res = new String[count];
        if (count >= 0) System.arraycopy(appo, 0, res, 0, count);
        return res;
    }


    public static JSONObject getJSONObject(Object obj) throws JSONException {
        return new JSONObject(obj.toString());
    }

    public static String getData(Object obj,String name) throws JSONException { //usefull for only string types
            JSONObject json = Utils.getJSONObject(obj);
            return json.getString(name);
    }




}
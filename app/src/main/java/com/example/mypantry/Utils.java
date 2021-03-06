package com.example.mypantry;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.mypantry.data.DBManager;
import com.example.mypantry.data.ui.fragment.HomeFragment;
import com.example.mypantry.item.Item;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Request;
import okio.Buffer;

public class Utils {

    public static boolean isConnect(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();
    }

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

        String[] appo = new String[20];
        int count = 0;
        for(int i = 0 ; i< list.length();i++){
            JSONObject obj = getJSONObject(list.get(i));
            if( count < 20) {
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

    public static DBManager getDBIstance(){
        return HomeFragment.getDB();
    }

    public static List<Item> getList(){
        return HomeFragment.getLst();
    }

}
package com.example.mypantry.connection;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Button;

import com.example.mypantry.R;

public class AuthToken {
    private static volatile String token = "-1";
    private static volatile String username = "-1";
    private static Context context;
    public static Boolean isNull(){
        return token.equals("-1") || username.equals("-1");
    }


    public static String getUsername(){
        return username;
    }

    public static String getToken(){
        return token;
    }

    public static Boolean assign(String tokn, String user){
        token = tokn;
        username = user;
        return !isNull();
    }

    public static Boolean saveToken(Context context) {
        if(token.equals("-1") && username.equals("-1")){
            return false;
        }
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.tokenDictionary),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.key_username), username);
        editor.putString(context.getString(R.string.key_token), token);
        editor.commit();
        return !isNull();

    }
    public static void deleteToken(){
        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.tokenDictionary),Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();
        cancel();
    }

    public static void importToken(Context contex){
        context = contex;
            if(isNull()) {
                SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.tokenDictionary), Context.MODE_PRIVATE);
                String tokn = pref.getString(context.getString(R.string.key_token), "-1");
                String usr = pref.getString(context.getString(R.string.key_username), "-1");

                if (!tokn.equals("-1") && !usr.equals("-1")) {
                    token = tokn;
                    username = usr;
                }
            }
    }

    private static void cancel(){
        token = "-1";
        username = "-1";
    }

}

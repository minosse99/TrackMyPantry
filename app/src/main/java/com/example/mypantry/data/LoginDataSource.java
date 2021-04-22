package com.example.mypantry.data;

import android.accounts.AccountManager;
import android.support.annotation.RequiresApi;
import android.util.JsonWriter;
import android.util.Log;
import android.widget.Toast;

import com.example.mypantry.data.model.LoggedInUser;

import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import javax.net.ssl.HttpsURLConnection;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {

    public Result<LoggedInUser> login(String username, String password) {

        try{

            // TODO: handle loggedInUser authentication
            JSONObject obj = new JSONObject();
            obj.put("username",username);
            obj.put("password",password);
            LoggedInUser user =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            username);
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }

}
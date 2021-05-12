package com.example.mypantry.data;

import android.util.Log;

import com.example.mypantry.data.model.LoggedInUser;
import com.example.mypantry.Network;

import java.io.IOException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource{
    private String username;
    private String email;
    private String password;

    public Result<LoggedInUser> login(String email, String password , String username) {
        this.username = username;
        this.password = password;
        this.email = email;
        LoggedInUser user = null;
        try {
            if(username != null){
                Network.register(username,email,password);
                user = new LoggedInUser(java.util.UUID.randomUUID().toString(),
                        email);

            }else
                user = Network.login(email,password);
            // TODO: handle loggedInUser authentication
            return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }


    public void logout() {
        // TODO: revoke authentication
    }


}

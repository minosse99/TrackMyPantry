package com.example.mypantry.data;

import android.util.Log;

import com.example.mypantry.data.model.LoggedInUser;
import com.example.mypantry.connection.*;

import java.io.IOException;

import javax.security.auth.login.LoginException;

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
            if(username != null) {
                Registration register = new Registration();
                String id = String.valueOf(register.execute(username, email, password));
                user = new LoggedInUser(id,AuthToken.getUsername());
            }else {
                user = AuthRequest.login(email, password);
            }
            Thread.sleep(800);                  //wait async call on Auth.login
            if(AuthToken.isNull())
                return new Result.Error(new LoginException("No account found"));
            else
                return new Result.Success<>(user);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
    }
}


    public void logout() {
        AuthToken.deleteToken();
    }

}

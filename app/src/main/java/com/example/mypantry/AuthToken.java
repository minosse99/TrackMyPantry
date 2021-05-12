package com.example.mypantry;

public class AuthToken {
    public static volatile String token;
    public static volatile String username;

    public static Boolean isNull(){
        return token == null || username == null;
    }
}

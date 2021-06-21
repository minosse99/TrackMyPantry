package com.example.mypantry;

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
}

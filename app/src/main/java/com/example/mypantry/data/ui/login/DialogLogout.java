package com.example.mypantry.data.ui.login;

import android.app.AlertDialog;
import android.content.Context;

public class DialogLogout {
    private final AlertDialog.Builder builder;

    public DialogLogout(Context context){
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Are you sure?");
        builder.setMessage("Do you want Log out from Tracky My Pantry?");
    }

    public AlertDialog.Builder get(){
        return builder;
    }


}

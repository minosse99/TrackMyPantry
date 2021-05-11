package com.example.mypantry.ui.login;

import com.example.mypantry.data.model.LoggedInUser;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;


    LoggedInUserView(String displayName, LoggedInUser user ) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }


}
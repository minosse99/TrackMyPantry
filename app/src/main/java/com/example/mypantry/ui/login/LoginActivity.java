package com.example.mypantry.ui.login;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v7.app.AppCompatActivity;
import android.text.BoringLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypantry.AuthToken;
import com.example.mypantry.Network;
import com.example.mypantry.R;
import com.example.mypantry.ui.login.LoginViewModel;
import com.example.mypantry.ui.login.LoginViewModelFactory;

import java.io.IOException;
import java.util.Set;
import java.util.prefs.Preferences;

public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;
    private TextView changeText;
    private Boolean login;
    @Override
    public void onCreate(Bundle savedInstanceState) {

        login = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginViewModel = new ViewModelProvider(this, new LoginViewModelFactory())
                .get(LoginViewModel.class);

        final EditText usernameEditText = findViewById(R.id.email);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);
        changeText = findViewById(R.id.changeTextView);

        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }
                if (loginFormState.getPasswordError() != null) {
                    passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
            }
        });

        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {

            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                if (loginResult == null) {
                    return;
                }
              loadingProgressBar.setVisibility(View.VISIBLE);//GONE);
                if (loginResult.getError() != null) {
                    showLoginFailed(loginResult.getError());
                }
                if (loginResult.getSuccess() != null) {
                    updateUiWithUser(loginResult.getSuccess());
                }
                setResult(Activity.RESULT_OK);

                //Complete and destroy login activity once successful
                finish();


            }
        });

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) { loginViewModel.loginDataChanged(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString());
            }
        };
        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loginViewModel.login(usernameEditText.getText().toString(),
                            passwordEditText.getText().toString(),getUsername());
                }
                return false;
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingProgressBar.setVisibility(View.VISIBLE);

                loginViewModel.login(usernameEditText.getText().toString(),
                        passwordEditText.getText().toString(),getUsername());

            }
        });
    }

    private Boolean checkLoginSave() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(getString(R.string.key_username),Context.MODE_PRIVATE);
        String tokn = pref.getString(getString(R.string.key_token),"-1");
        String usr = pref.getString(getString(R.string.key_username),"-1");
        if(!tokn.equals("-1") && !usr.equals("-1")){
            AuthToken.token = tokn;
            AuthToken.username = usr;
            Log.d("DONE it","DONE IT");
            return true;

        }else{
            Log.d("false","false");
            return false;

        }
    }


    public void storePreferences() {
        while(AuthToken.token == null){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(getString(R.string.username), AuthToken.username);
        editor.commit();

        checkLoginSave();

    }
    private void updateUiWithUser(LoggedInUserView model){
        String welcome = getString(R.string.welcome) + model.getDisplayName();
        // TODO : initiate successful logged in experience
        storePreferences();
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


    public void changeRegisterLogIn(View view) {

        EditText btnUsernameVisible = findViewById(R.id.username);
      if(changeText.getText() == getText(R.string.already_register_log_in)){
            login= true;
            changeText.setText(getText(R.string.sign_in));
            btnUsernameVisible.setVisibility(View.INVISIBLE);
        }else{
            login = false;
            changeText.setText(getText(R.string.already_register_log_in));
            btnUsernameVisible.setVisibility(View.VISIBLE);
        }
    }

    private String getUsername(){
        EditText btnUsername = findViewById(R.id.username);
        String username;
        if(login){
            return null;
        }else{
            username = btnUsername.getText().toString();
            return username;
        }
    }



}
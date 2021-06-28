package com.example.mypantry.activity;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mypantry.ActivitySettings;
import com.example.mypantry.connection.AuthToken;
import com.example.mypantry.R;
import com.example.mypantry.ui.login.DialogLogout;
import com.example.mypantry.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DialogLogout dialout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialout = new DialogLogout(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewItem(view);
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_shoppinglist, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);


    }


    @Override
    protected void onStart() {
        AuthToken.importToken(this);
        checkAuthUI();
        super.onStart();
    }

    @Override
    protected void onStop() {
        AuthToken.saveToken(this);
        super.onStop();
    }


    public void loginAction(View view) {
        if(!AuthToken.isNull()){
                AlertDialog.Builder b = dialout.get();
                b.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    AuthToken.deleteToken();
                    checkAuthUI();
                }});
                b.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {;}
                }).create().show();

        }else {
            Intent intent = new Intent();
            ComponentName component =
                    new ComponentName(this, LoginActivity.class);
            intent.setComponent(component);
            startActivity(intent);
        }
    }

    public void addNewItem(View view){
        Intent intent = new Intent();
        ComponentName component =
                new ComponentName(this, ActivitySearch.class);
        intent.setComponent(component);
        startActivity(intent);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =
                    new NotificationChannel("CHANNEL_ID", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void checkAuthUI(){
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        ImageView navImage = (ImageView) headerView.findViewById(R.id.imageView);
        Button btnLogin = (Button) headerView.findViewById(R.id.buttonLogin);
        TextView usernameTxt = (TextView) headerView.findViewById(R.id.userTextView);
        if(AuthToken.isNull()) {  //user is not authenticated
            navImage.setVisibility(View.INVISIBLE);
           usernameTxt.setVisibility(View.INVISIBLE);
            usernameTxt.setText(" ");
            btnLogin.setText("Login");
        }else {                     //user authenticated
            usernameTxt.setVisibility(View.VISIBLE);
            usernameTxt.setText(AuthToken.getUsername());
            navImage.setVisibility(View.VISIBLE);
            btnLogin.setText("Log Out");
        }
    }

    public void goToSettings(MenuItem item) {
        Intent intent = new Intent();
        ComponentName component = new ComponentName(this, ActivitySettings.class);
        intent.setComponent(component);
        startActivity(intent);
    }
}
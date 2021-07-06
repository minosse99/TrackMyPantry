package com.example.mypantry.activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.mypantry.ReminderBroadcast;
import com.example.mypantry.connection.AuthToken;
import com.example.mypantry.R;
import com.example.mypantry.data.ui.fragment.HomeFragment;
import com.example.mypantry.data.ui.login.DialogLogout;
import com.example.mypantry.data.ui.login.LoginActivity;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DialogLogout dialout;

    protected void onCreate(Bundle savedInstanceState) {
        Log.e("Current Time", String.valueOf(System.currentTimeMillis()));
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
                R.id.nav_home, R.id.nav_shoppinglist)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        createNotificationChannel();

    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction transaction = fm.beginTransaction();
            HomeFragment homeFragment = new HomeFragment();
            transaction.replace(R.id.nav_view, homeFragment);
            transaction.addToBackStack("FragmentHome");
            transaction.commit();
        }
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        AuthToken.importToken(this);
        checkAuthUI();
        super.onStart();
    }

    @Override
    protected void onStop() {
        Toast.makeText(this,"onStop()",Toast.LENGTH_SHORT).show();
        AuthToken.saveToken(this);
        Intent intent = new Intent(MainActivity.this, ReminderBroadcast.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this,0,intent,0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        long time = System.currentTimeMillis();
        long minute = 1000 * 60;  //to set an alarmManager over 1 minute
        alarmManager.set(AlarmManager.RTC_WAKEUP,time + (minute * 120), pendingIntent);

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


    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "ReminderNotification";
            String description = "Set reminder for empty pantry";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel =
                    new NotificationChannel("TRACKPANTRY", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager =
                    getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
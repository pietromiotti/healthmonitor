package com.example.healthmonitor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.appcompat.widget.Toolbar;

import com.example.healthmonitor.RoomDatabase.DatabaseManager;
import com.example.healthmonitor.utils.NotificationHandler;
import com.example.healthmonitor.utils.PreferenceManager;
import com.google.android.material.navigation.NavigationView;

import java.lang.ref.WeakReference;

public class MainActivity extends AppCompatActivity {
    NavController nav;
    AppBarConfiguration appBarConfiguration;
    NavigationView navView;
    DrawerLayout drawer;
    Toolbar toolbar;
    NotificationHandler notificationHandler;
    public DatabaseManager databaseManager;
    public PreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nav = Navigation.findNavController(this, R.id.nav_host_fragment);
        navView = findViewById(R.id.nav_view);
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(nav.getGraph()).setDrawerLayout(drawer).build();
        //NavigationUI.setupWithNavController(navView, nav);
        NavigationUI.setupActionBarWithNavController(this, nav, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, nav);
        this.preferenceManager = PreferenceManager.getPreferenceManagerWithContext(getApplicationContext());
        this.databaseManager = DatabaseManager.getInstanceDb(this);
        this.notificationHandler = NotificationHandler.getInstanceOfNotificationHandler(getApplicationContext());

    }


    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
       // databaseManager.Close();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

}


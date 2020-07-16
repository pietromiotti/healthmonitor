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
        /*Setting della toolbar*/
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        appBarConfiguration = new AppBarConfiguration.Builder(nav.getGraph()).setDrawerLayout(drawer).build();

        /*Setup navigation View -> Genera l'hamburger button per l'apertura del Drawer*/
        NavigationUI.setupActionBarWithNavController(this, nav, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, nav);

        /*Init dei manager*/
        this.preferenceManager = PreferenceManager.getPreferenceManagerWithContext(getApplicationContext());
        this.databaseManager = DatabaseManager.getInstanceDb(this);
        this.notificationHandler = NotificationHandler.getInstanceOfNotificationHandler(getApplicationContext());

    }


    /*
    Questa funzione permette il corretto funzionamento della toolbar, in particolare permette la navigazione all'interno del navigation Menù e gestisce,
    con la libreria NavigationUI, da sè le animazioni dell'Hamburger Button (Freccia Indietro, Hamburger Button)
     */
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

}


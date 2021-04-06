package com.livingstonei2p.XDAndroid;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import lib.folderpicker.FolderPicker;

public class MainActivity2 extends AppCompatActivity {
    private Thread logger;
    private AppBarConfiguration mAppBarConfiguration;
    private BufferedReader LogXd;
    private Process xd=null;
    public static String logXdBuf = new String();
    private Intent intent_folderChooser;
    private final int FOLDERPICKER_CODE = 50;
    private Toolbar mToolBar;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FOLDERPICKER_CODE && resultCode == Activity.RESULT_OK) {

            String folderLocation = data.getExtras().getString("data");
            Log.i( "folderLocation", folderLocation );
            try {
                MainActivity.updateWorkDirectory(folderLocation);
                Toast.makeText(this,"Please restart the app", Toast.LENGTH_LONG).show();

              //  ProcessPhoenix.triggerRebirth(this.getBaseContext());
            }catch(Exception er){
                Toast.makeText(this,"Cant select directory!"+er.toString()
                        , Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);
            Log.d("DEBUG", "On Create");

            while (xd == null) {
                Log.d("DEBUG", "Main activity2, not found xd");
                xd = MainActivity.xd;
            }
            LogXd = new BufferedReader(new InputStreamReader(xd.getInputStream()));
            Log.d("DEBUG", "LogXd is inited");
            setContentView(R.layout.activity_main2);

            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    WebView webView = findViewById(R.id.WebView);
                    //   webView.setVisibility(View.VISIBLE);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient());
                    webView.loadUrl("http://127.0.0.1:1776/");

                }
            });
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            logger = new Thread(new Runnable() {
                public void run() {
                    // a potentially time consuming task
                    TextView lg = findViewById(R.id.LogEditText);
                    lg.setText(logXdBuf);
                    while (true) {
                        try {
                            logXdBuf += LogXd.readLine() + "\n";
                            if (logXdBuf.length() > 3000) logXdBuf =
                                    logXdBuf.substring(1000, 3000);
                            lg.setText(logXdBuf);
                            Log.d("DEBUG", "logedittext was changed" + lg.getText());
                        } catch (IOException exception) {
                            lg.setText(exception.toString());
                        } catch (Throwable exception) {
                            lg.setText(exception.toString());

                        }
                    }
                }
            });
            mToolBar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(mToolBar);
            intent_folderChooser = new Intent(this, FolderPicker.class);
            Log.d("XDMenu","Init on Menu Item Click Listener");
            mToolBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    if (item.getItemId() == R.id.action_settings) {
                        Log.d("XDMenu", "FoolderChooser");
                        startActivityForResult(intent_folderChooser, FOLDERPICKER_CODE);

                    }
                    return false;
                }
            });

            logger.start();
        }catch(Throwable e){
            logXdBuf=e.toString();
        }
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
      /*  mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();*/
        //NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        //NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity2, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}
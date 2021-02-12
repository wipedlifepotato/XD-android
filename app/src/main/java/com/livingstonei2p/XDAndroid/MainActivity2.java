package com.livingstonei2p.XDAndroid;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

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

public class MainActivity2 extends AppCompatActivity {
    private Thread logger;
    private AppBarConfiguration mAppBarConfiguration;
    private BufferedReader LogXd;
    private Process xd=null;
    public static String logXdBuf = new String();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("DEBUG","On Create");

        while(xd == null) {
            Log.d("DEBUG","Main activity2, not found xd");
            xd = MainActivity.xd;
        }
        LogXd = new BufferedReader(new InputStreamReader(xd.getInputStream()));
        Log.d("DEBUG","LogXd is inited");
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
                while(true) {
                    try {
                        logXdBuf += LogXd.readLine()+"\n";
                        if (logXdBuf.length() > 3000) logXdBuf=
                                logXdBuf.substring(1000,3000);
                        lg.setText(logXdBuf);
                        Log.d("DEBUG", "logedittext was changed"+lg.getText());
                    } catch (IOException exception) {
                        lg.setText(exception.toString());
                    } catch( Throwable  exception ){
                        lg.setText(exception.toString());

                    }
                }
            }
        });
        logger.start();

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
package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

public class internetnotavailable extends AppCompatActivity implements View.OnClickListener {
    Button onNet;
    View v;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internetnotavailable);

        //This code sets the status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));

        //This part of the code displays the statusbar icon color to black
        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);

        onNet = findViewById(R.id.buttonOnInternet);
        onNet.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        startActivity(settingsIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInternetAvailable()){
            finish();
        }
        else {
            onBackPressed();
        }
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Turn on internet to go back",Toast.LENGTH_LONG).show();
    }
}
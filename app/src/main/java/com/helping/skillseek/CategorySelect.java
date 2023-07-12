package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Vibrator;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

public class CategorySelect extends AppCompatActivity implements View.OnClickListener {

    ImageButton hirerBtn, workerBtn;
    Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_select);
        //This code sets the status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.whiteDDDDD));
        window.setNavigationBarColor(this.getResources().getColor(R.color.whiteDDDDD));

        //This part of the code displays the statusbar icon color to black
        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);

        hirerBtn = findViewById(R.id.imageButtonManager);
        workerBtn = findViewById(R.id.imageButtonWorker);
        hirerBtn.setOnClickListener(this);
        workerBtn.setOnClickListener(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageButtonManager){
            Toast.makeText(this, "You are a manager", Toast.LENGTH_SHORT).show();
            vibrator.vibrate(5);
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("category","hirer");
            editor.apply();
            Intent intent = new Intent(this, hirer_main.class);
            startActivity(intent);

        } else if (view.getId() == R.id.imageButtonWorker) {
            Toast.makeText(this, "You are a worker", Toast.LENGTH_SHORT).show();
            vibrator.vibrate(5);
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("category","hiree");
            editor.apply();
            Intent intent = new Intent(this, Hiree_main.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String fetchedCategory = sharedPreferences.getString("category","default");
        if (fetchedCategory.equals("hirer")) {
            Intent intent = new Intent(this, hirer_main.class);
            startActivity(intent);
        } else if (fetchedCategory.equals("hiree")) {
            Intent intent = new Intent(this, Hiree_main.class);
            startActivity(intent);
        }
    }
}
package com.helping.skillseek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CategorySelect extends AppCompatActivity implements View.OnClickListener {

    ImageButton hirerBtn, workerBtn;
    Vibrator vibrator;
    String number;
    FirebaseAuth auth;
    DatabaseReference hirerRef, hireeRef;
    String uid;
    boolean exists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_select);
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();


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
        Intent intent = getIntent();
        number = intent.getStringExtra("phoneNumber");

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageButtonManager) {
            vibrator.vibrate(2);
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("category", "hirer");
            editor.apply();
            Intent intent = new Intent(this, hirer_main.class);
            startActivity(intent);

        } else if (view.getId() == R.id.imageButtonWorker) {
            vibrator.vibrate(2);
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("category", "hiree");
            editor.apply();
            Intent intent = new Intent(this, Hiree_main.class);
            startActivity(intent);
        }
    }
}
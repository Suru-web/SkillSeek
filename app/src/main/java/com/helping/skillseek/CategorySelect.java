package com.helping.skillseek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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

        auth = FirebaseAuth.getInstance();

        uid = auth.getCurrentUser().getUid();
        hireeRef = FirebaseDatabase.getInstance().getReference().child("hiree");
        hirerRef = FirebaseDatabase.getInstance().getReference().child("hirer");

        if (currentUser != null) {
            uid = currentUser.getUid();
            hirerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String nodeKey = childSnapshot.getKey();
                        if (nodeKey.equals(uid)) {
                            Toast.makeText(CategorySelect.this,"User Exists",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CategorySelect.this, homepage.class);
                            startActivity(intent);
                            exists = true;
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            });
            hireeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String nodeKey = childSnapshot.getKey();
                        if (nodeKey.equals(uid)) {
                            Toast.makeText(CategorySelect.this,"User Exists",Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(CategorySelect.this, homepage.class);
                            startActivity(intent);
                            exists = true;
                            return;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            });

        }

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.imageButtonManager) {
            vibrator.vibrate(5);
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("category", "hirer");
            editor.apply();
            Intent intent = new Intent(this, hirer_main.class);
            startActivity(intent);

        } else if (view.getId() == R.id.imageButtonWorker) {
            vibrator.vibrate(5);
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("category", "hiree");
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
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            uid = currentUser.getUid();
            hirerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String nodeKey = childSnapshot.getKey();
                        if (nodeKey.equals(uid)) {
                            Intent intent = new Intent(CategorySelect.this, homepage.class);
                            startActivity(intent);
                            return;
                        }
                    }
                    // User does not exist under "hirer" node, proceed to check "hiree" node
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            });
            hireeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                        String nodeKey = childSnapshot.getKey();
                        if (nodeKey.equals(uid)) {
                            Intent intent = new Intent(CategorySelect.this, homepage.class);
                            startActivity(intent);
                            return;
                        }
                    }
                    // User does not exist under "hiree" node
                    // You can decide the appropriate action here
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle any errors
                }
            });

        }
    }
}
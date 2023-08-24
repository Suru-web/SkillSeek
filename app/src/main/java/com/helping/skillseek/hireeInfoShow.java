package com.helping.skillseek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class hireeInfoShow extends AppCompatActivity implements View.OnClickListener {
    CircleImageView profilepic;
    TextView name,skill,phone,username,rating,place;

    DatabaseReference databaseReference;
    ImageButton call,whatsapp,message,backbtn;
    Vibrator vibrator;
    private static final int CALL_PERMISSION_REQUEST = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiree_info_show);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));

        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);


        Intent intent = getIntent();
        String id =  intent.getStringExtra("hireeListId");

        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        name = findViewById(R.id.nameVP);
        skill = findViewById(R.id.skillVP);
        phone = findViewById(R.id.phoneVp);
        username = findViewById(R.id.usernameVp);
        rating = findViewById(R.id.ratingVP);
        place = findViewById(R.id.locationVP);
        profilepic = findViewById(R.id.profilepicVP);
        call = findViewById(R.id.callBtnVP);
        whatsapp = findViewById(R.id.whatsappBtnVP);
        message = findViewById(R.id.messageBtnVP);
        backbtn = findViewById(R.id.backbtnVP);

        databaseReference = FirebaseDatabase.getInstance().getReference("hiree").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String naam = snapshot.child("name").getValue(String.class);
                    String url = snapshot.child("downloadUrl").getValue(String.class);
                    String kaam = snapshot.child("skill").getValue(String.class);
                    String uname = snapshot.child("username").getValue(String.class);

                    name.setText(naam);
                    Picasso.get()
                            .load(url)
                            .placeholder(R.drawable.profilepicture)
                            .error(R.drawable.profilepicture)
                            .into(profilepic);
                    skill.setText(kaam);
                    username.setText(uname);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(hireeInfoShow.this,"Failed to load data",Toast.LENGTH_SHORT).show();
            }
        });

        call.setOnClickListener(this);
        whatsapp.setOnClickListener(this);
        message.setOnClickListener(this);
        backbtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String phoneNumber = phone.getText().toString().trim();
        if (view.getId() == R.id.callBtnVP) {
            vibrator.vibrate(2);
            if (ContextCompat.checkSelfPermission(hireeInfoShow.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initiate the call
                makeCall(phoneNumber);
            } else {
                // Request the CALL_PHONE permission
                ActivityCompat.requestPermissions(hireeInfoShow.this, new String[]{android.Manifest.permission.CALL_PHONE}, CALL_PERMISSION_REQUEST);
            }
        }



        else if (view.getId() == R.id.whatsappBtnVP) {
            vibrator.vibrate(2);
            if (checkInstallation(hireeInfoShow.this, "com.whatsapp")) {
                // on below line displaying a toast message if maps is installed.
                Intent whatsappIntent = new Intent(Intent.ACTION_SENDTO, Uri.parse("smsto:" + phoneNumber));
                whatsappIntent.setPackage("com.whatsapp");
                startActivity(whatsappIntent);
            } else {
                Toast.makeText(hireeInfoShow.this, "Whatsapp is not installed", Toast.LENGTH_SHORT).show();
            }
        }



        else if (view.getId()==R.id.messageBtnVP) {
            vibrator.vibrate(2);
            Intent intent = new Intent(Intent.ACTION_VIEW,Uri.fromParts("sms",phoneNumber,null));
            startActivity(intent);
        }


        else if (view.getId()==R.id.backbtnVP){
            vibrator.vibrate(2);
            finish();
        }

    }

    //This is to check if the package is installed
    public static boolean checkInstallation(Context context, String packageName) {
        // on below line creating a variable for package manager.
        PackageManager pm = context.getPackageManager();
        try {
            // on below line getting package info
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            // on below line returning true if package is installed.
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            // returning false if package is not installed on device.
            return false;
        }
    }


    private void makeCall(String phoneNumber) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + phoneNumber));
        startActivity(callIntent);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CALL_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted, initiate the call
                makeCall(phone.getText().toString().trim());
            } else {
                // Permission denied
                Toast.makeText(this, "Call permission declined", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
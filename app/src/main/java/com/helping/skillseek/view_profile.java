package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class view_profile extends AppCompatActivity implements View.OnClickListener {
    ImageButton backButton;
    Vibrator vibrator;
    CircleImageView profpic;
    TextView nameDisplay, usernameDisplay,phoneDisplay,emailDispay,addressDisplay,idDisplay;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));

        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String phoneNumber = user.getPhoneNumber();

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        backButton = findViewById(R.id.goback);
        profpic = findViewById(R.id.ppic);
        nameDisplay = findViewById(R.id.nameDisp);
        usernameDisplay = findViewById(R.id.usernameDisp);
        phoneDisplay = findViewById(R.id.phonenumDisp);
        emailDispay = findViewById(R.id.emailDisp);
        addressDisplay = findViewById(R.id.addressDisp);
        idDisplay = findViewById(R.id.uID);

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String profilePicImage = sharedPreferences.getString("imageurl","default");
        String id = sharedPreferences.getString("uniqueID","defualt");
        String yourCategory = sharedPreferences.getString("category"," ");


        if (profilePicImage!=null && !profilePicImage.isEmpty()) {
            Picasso.get()
                    .load(profilePicImage)
                    .placeholder(R.drawable.profilepicture)
                    .error(R.drawable.profilepicture)
                    .into(profpic);
        }
        else {
            Toast.makeText(this,"Profile Pic not found",Toast.LENGTH_SHORT).show();
        }
        phoneDisplay.setText(phoneNumber);



        if (yourCategory.equals("Hirer")||yourCategory.equals("hirer")) {
            Log.d("Testing",yourCategory);
            databaseReference = FirebaseDatabase.getInstance().getReference("hirer").child(id);
        }
        else if (yourCategory.equals("hiree")||yourCategory.equals("Hiree")){
            Log.d("Testing",yourCategory);
            databaseReference = FirebaseDatabase.getInstance().getReference("hiree").child(id);
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String uname = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    String uniqueID = snapshot.child("id").getValue(String.class);

                    nameDisplay.setText(name);
                    usernameDisplay.setText(uname);
                    emailDispay.setText(email);
                    addressDisplay.setText(address);
                    idDisplay.setText("Unique ID : "+uniqueID);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view_profile.this,"Facing some error in fetching the data",Toast.LENGTH_SHORT).show();
            }
        });

        backButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        vibrator.vibrate(2);
        finish();
    }

}
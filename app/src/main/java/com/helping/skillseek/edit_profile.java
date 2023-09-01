package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_profile extends AppCompatActivity {
    EditText name,username,age,skill;
    CircleImageView pfp,pfpedit;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    int check;
    String loadImage;
    String id,categ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));

        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);

        name = findViewById(R.id.nameEditInEP);
        username = findViewById(R.id.usernameEditInEP);
        age = findViewById(R.id.ageEditInEP);
        skill = findViewById(R.id.skillEditInEP);
        pfp = findViewById(R.id.pfpInEP);
        pfpedit = findViewById(R.id.pfpeditInEP);

        auth = FirebaseAuth.getInstance();
        id = auth.getCurrentUser().getUid();
        Intent intent = getIntent();
        categ = intent.getStringExtra("editProfileGetCategory");

        if (categ.equals("Hirer")||categ.equals("hirer")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("hirer").child(id);
            check = 1;
        }
        else if (categ.equals("hiree")||categ.equals("Hiree")){
            databaseReference = FirebaseDatabase.getInstance().getReference("hiree").child(id);
            check = 0;
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()&&check==1) {
                    String gname = snapshot.child("name").getValue(String.class);
                    String uname = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    String address = snapshot.child("address").getValue(String.class);
                    loadImage = snapshot.child("downloadUrl").getValue(String.class);

                    name.setText(gname);
                    username.setText(uname);
                    skill.setText(email);
                    age.setText(address);
                    if (loadImage!=null && !loadImage.isEmpty()) {
                        Picasso.get()
                                .load(loadImage)
                                .placeholder(R.drawable.profilepicture)
                                .error(R.drawable.profilepicture)
                                .into(pfp);
                    }
                    else {
                        Toast.makeText(edit_profile.this,"Profile Pic not found",Toast.LENGTH_SHORT).show();
                    }
                }
                else if (snapshot.exists()&&check==0) {
                    String gname = snapshot.child("name").getValue(String.class);
                    String uname = snapshot.child("username").getValue(String.class);
                    String gskill = snapshot.child("skill").getValue(String.class);
                    String gage = snapshot.child("age").getValue(String.class);
                    loadImage = snapshot.child("downloadUrl").getValue(String.class);

                    name.setText(gname);
                    username.setText(uname);
                    skill.setText(gskill);
                    age.setText(gage);
                    if (loadImage!=null && !loadImage.isEmpty()) {
                        Picasso.get()
                                .load(loadImage)
                                .placeholder(R.drawable.profilepicture)
                                .error(R.drawable.profilepicture)
                                .into(pfp);
                    }
                    else {
                        Toast.makeText(edit_profile.this,"Profile Pic not found",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(edit_profile.this,"Facing some error in fetching the data",Toast.LENGTH_SHORT).show();
            }
        });

    }
}
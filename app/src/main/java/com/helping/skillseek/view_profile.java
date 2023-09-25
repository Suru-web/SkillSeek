package com.helping.skillseek;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.RectF;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class view_profile extends AppCompatActivity implements View.OnClickListener {
    ImageButton backButton,menubtn;
    Vibrator vibrator;
    CircleImageView profpic;
    TextView nameDisplay, usernameDisplay,phoneDisplay,emailDispay,addressDisplay,idDisplay,emailM,addressM;
    private DatabaseReference databaseReference;
    int check;
    String loadImage;
    FirebaseAuth auth;
    String yourCategory;
    ImageView profilePic;

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

        auth = FirebaseAuth.getInstance();
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
        emailM = findViewById(R.id.email);
        addressM = findViewById(R.id.address);
        menubtn = findViewById(R.id.menuBtn);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        yourCategory = sharedPreferences.getString("category"," ");                  //instead of checking if hirer/hiree fetch from database

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        Log.d("UID : ",uid);


        if(!phoneNumber.isEmpty()) {
            phoneDisplay.setText(phoneNumber);
        }
        else {
            phoneDisplay.setText("Phone Number");
        }
        if (yourCategory.equals("Hirer")||yourCategory.equals("hirer")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("hirer").child(uid);
            databaseReference.keepSynced(true);
            check = 1;
        }
        else if (yourCategory.equals("hiree")||yourCategory.equals("Hiree")){
            databaseReference = FirebaseDatabase.getInstance().getReference("hiree").child(uid);
            databaseReference.keepSynced(true);
            check = 0;
        }
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()&&check==1) {
                    String name = snapshot.child("name").getValue(String.class);
                    String uname = snapshot.child("username").getValue(String.class);
                    String email = snapshot.child("email").getValue(String.class);
                    if (email.length() > 10) {  // Check if the email length is greater than 23 characters
                        email = email.substring(0, 7) + "..." + email.substring(email.length() - 3);
                    }
                    String address = snapshot.child("address").getValue(String.class);
                    loadImage = snapshot.child("downloadUrl").getValue(String.class);

                    nameDisplay.setText(name);
                    usernameDisplay.setText(uname);
                    emailDispay.setText(email);
                    addressDisplay.setText(address);
                    idDisplay.setText("Unique ID : "+uid);
                    if (loadImage!=null && !loadImage.isEmpty()) {
                        Picasso.get()
                                .load(loadImage)
                                .placeholder(R.drawable.profilepicture)
                                .error(R.drawable.profilepicture)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(profpic, new Callback() {
                                    @Override
                                    public void onSuccess() {}
                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get()
                                                .load(loadImage)
                                                .placeholder(R.drawable.profilepicture)
                                                .error(R.drawable.profilepicture)
                                                .into(profpic);
                                    }
                                });
                    }
                    else {
                        Toast.makeText(view_profile.this,"Profile Pic not found",Toast.LENGTH_SHORT).show();
                    }
                }
                else if (snapshot.exists()&&check==0) {
                    String name = snapshot.child("name").getValue(String.class);
                    String uname = snapshot.child("username").getValue(String.class);
                    String skill = snapshot.child("skill").getValue(String.class);
                    String age = snapshot.child("age").getValue(String.class);
                    loadImage = snapshot.child("downloadUrl").getValue(String.class);

                    nameDisplay.setText(name);
                    usernameDisplay.setText(uname);
                    emailDispay.setText(skill);
                    addressDisplay.setText(age);
                    idDisplay.setText("Unique ID : "+uid);
                    emailM.setText("Skill :- ");
                    addressM.setText("Age :-");
                    if (loadImage!=null && !loadImage.isEmpty()) {
                        Picasso.get()
                                .load(loadImage)
                                .placeholder(R.drawable.profilepicture)
                                .error(R.drawable.profilepicture)
                                .networkPolicy(NetworkPolicy.OFFLINE)
                                .into(profpic, new Callback() {
                                    @Override
                                    public void onSuccess() {}
                                    @Override
                                    public void onError(Exception e) {
                                        Picasso.get()
                                                .load(loadImage)
                                                .placeholder(R.drawable.profilepicture)
                                                .error(R.drawable.profilepicture)
                                                .networkPolicy(NetworkPolicy.OFFLINE)
                                                .into(profpic);
                                    }
                                });
                    }
                    else {
                        Toast.makeText(view_profile.this,"Profile Pic not found",Toast.LENGTH_SHORT).show();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(view_profile.this,"Facing some error in fetching the data",Toast.LENGTH_SHORT).show();
            }
        });


        backButton.setOnClickListener(this);
        menubtn.setOnClickListener(this);
        profpic.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.goback) {
            vibrator.vibrate(2);
            finish();
        }
        else if (v.getId()==R.id.menuBtn) {
            vibrator.vibrate(2);
            if (check == 1) {

                PopupMenu popupMenu = new PopupMenu(view_profile.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.porfile_dropdown, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.editProfileBtn) {
                            Intent intent = new Intent(view_profile.this, edit_profile.class);
                            intent.putExtra("editProfileGetCategory", yourCategory);
                            startActivity(intent);
                            return true;
                        } else if (item.getItemId() == R.id.logoutBtn) {
                            auth.signOut();
                            Intent intent = new Intent(view_profile.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        } else if (item.getItemId() == R.id.savedBtn) {
                            Intent intent = new Intent(view_profile.this, saved_list.class);
                            startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            } else if (check == 0) {
                PopupMenu popupMenu = new PopupMenu(view_profile.this, v);
                popupMenu.getMenuInflater().inflate(R.menu.if_hiree_profile_dropdown, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @SuppressLint("NonConstantResourceId")
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.editProfileBtn) {
                            Intent intent = new Intent(view_profile.this, edit_profile.class);
                            intent.putExtra("editProfileGetCategory", yourCategory);
                            startActivity(intent);
                            return true;
                        } else if (item.getItemId() == R.id.logoutBtn) {
                            auth.signOut();
                            Intent intent = new Intent(view_profile.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            return true;
                        }
                        return false;
                    }
                });
                popupMenu.show();
            }
        } else if (v.getId()==R.id.ppic) {
            vibrator.vibrate(2);
            new profile_picture_zoom_view(profilePic,loadImage,this);
        }
    }

}
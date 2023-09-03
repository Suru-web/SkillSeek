package com.helping.skillseek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

import de.hdodenhof.circleimageview.CircleImageView;

public class hireeInfoShow extends AppCompatActivity implements View.OnClickListener {
    CircleImageView profilepic;
    TextView name,skill,phone,username,place;

    DatabaseReference databaseReference;
    ImageButton call,whatsapp,message,backbtn;
    Vibrator vibrator;
    LottieAnimationView likeButton;
    boolean liked = false;
    DatabaseReference saveLiked;
    FirebaseAuth auth;
    private static final int CALL_PERMISSION_REQUEST = 100;
    String adminID;
    String yourCategory;
    String countCount;
    DatabaseReference countAddHiree;
    RatingBar rateUser;
    LottieAnimationView eye;
    TextView countTEXT,rateBtn,ratingDisplay,raterCnt,ratingDone;
    DatabaseReference ratingRef;
    boolean ratedAlready;
    LinearLayout linearLayout;
    ImageView profilePic;
    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiree_info_show);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));
        likeButton = findViewById(R.id.likeButtonVP);

        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);


        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        name = findViewById(R.id.nameVP);
        skill = findViewById(R.id.skillVP);
        phone = findViewById(R.id.phoneVp);
        username = findViewById(R.id.usernameVp);
        place = findViewById(R.id.locationVP);
        profilepic = findViewById(R.id.profilepicVP);
        call = findViewById(R.id.callBtnVP);
        whatsapp = findViewById(R.id.whatsappBtnVP);
        message = findViewById(R.id.messageBtnVP);
        backbtn = findViewById(R.id.backbtnVP);
        countTEXT = findViewById(R.id.countTV);
        eye = findViewById(R.id.eyesLook);
        rateUser = findViewById(R.id.rateBar);
        rateBtn = findViewById(R.id.rateBtnTV);
        ratingDisplay = findViewById(R.id.ratingVP);
        raterCnt = findViewById(R.id.raterCount);
        ratingDone = findViewById(R.id.alreadyRatedTV);
        linearLayout = findViewById(R.id.llrate);

        Intent intent = getIntent();
        String id =  intent.getStringExtra("hireeListId");
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        yourCategory = sharedPreferences.getString("category"," ");


        countAddHiree = FirebaseDatabase.getInstance().getReference("COUNT").child(id);
        countAddHiree.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    countCount = snapshot.child("count").getValue(String.class);
                    System.out.println(countCount);
                    int c = Integer.parseInt(countCount);
                    c = c+1;
                    String newCount = String.valueOf(c);
                    addCount add = new addCount(newCount);
                    countAddHiree.setValue(add);
                    eye.setRepeatCount(10);
                    eye.setSpeed(0.5f);
                    eye.playAnimation();
                    countTEXT.setText(newCount);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        auth = FirebaseAuth.getInstance();
        adminID = auth.getCurrentUser().getUid();

        if (yourCategory.equals("Hirer")||yourCategory.equals("hirer")) {
            saveLiked = FirebaseDatabase.getInstance().getReference("LIKED").child(adminID).child(id);
            saveLiked.child("liked").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        likeButton.setMinAndMaxProgress(0.5f, 0.5f);
                        likeButton.playAnimation();
                        liked = true;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });
        }
        else {
            likeButton.setVisibility(View.GONE);
            linearLayout.setVisibility(View.GONE);
        }


        databaseReference = FirebaseDatabase.getInstance().getReference("hiree").child(id);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String naam = snapshot.child("name").getValue(String.class);
                    url = snapshot.child("downloadUrl").getValue(String.class);
                    String kaam = snapshot.child("skill").getValue(String.class);
                    String uname = snapshot.child("username").getValue(String.class);
                    String number = snapshot.child("phoneNumber").getValue(String.class);

                    name.setText(naam);
                    Picasso.get()
                            .load(url)
                            .placeholder(R.drawable.profilepicture)
                            .error(R.drawable.profilepicture)
                            .into(profilepic);
                    skill.setText(kaam);
                    username.setText(uname);
                    phone.setText(number);
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

        profilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vibrator.vibrate(2);
                new profile_picture_zoom_view(profilePic,url,hireeInfoShow.this);
            }
        });


        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liked==true){
                    vibrator.vibrate(2);
                    if (!id.isEmpty()){
                        saveLiked.removeValue();
                    }
                    likeButton.setMinAndMaxProgress(0.5f,1.0f);             //this means the disliked
                    likeButton.playAnimation();
                    liked = false;
                }
                else {
                    vibrator.vibrate(2);
                    savedAccount savedAccount = new savedAccount(liked,id);           //this means liked
                    saveLiked.setValue(savedAccount);
                    likeButton.setMinAndMaxProgress(0.0f,0.5f);
                    likeButton.playAnimation();
                    liked = true;
                }
            }
        });
        ratingRef = FirebaseDatabase.getInstance().getReference("rating").child(id);
        System.out.println(id);
        ratingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    Float avgRate = snapshot.child("rate").getValue(Float.class);
                    Float cnt = snapshot.child("raterCount").getValue(Float.class);
                    DecimalFormat decimalFormat = new DecimalFormat("#.#");
                    DecimalFormat decimalFormatcnt = new DecimalFormat("#");
                    String avgr = decimalFormat.format(avgRate);
                    String newCnt = decimalFormatcnt.format(cnt);
                    ratingDisplay.setText(avgr);
                    raterCnt.setText("Total rating : "+newCnt);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        rateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(id);
                rateUser.setVisibility(View.VISIBLE);
                if (!ratedAlready) {
                    rateUser.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                        @Override
                        public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                            Toast.makeText(hireeInfoShow.this, "Your rating of " + rating+" has added", Toast.LENGTH_LONG).show();
                            ratingRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if (snapshot.exists()) {
                                        Float avgRate = snapshot.child("rate").getValue(Float.class);
                                        Float totalCount = snapshot.child("raterCount").getValue(Float.class);
                                        Float newAvgRate = ((avgRate * totalCount) + rating) / (totalCount + 1);
                                        totalCount = totalCount + 1;
                                        rateObject rate = new rateObject(newAvgRate, totalCount);
                                        ratingRef.setValue(rate);
                                        String avgr = newAvgRate.toString();
                                        ratingDisplay.setText(avgr);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });
                            rateUser.setVisibility(View.GONE);
                        }
                    });
                    ratedAlready = true;
                }
                else {
                    rateUser.setVisibility(View.GONE);
                    ratingDone.setVisibility(View.VISIBLE);
                }
            }
        });
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
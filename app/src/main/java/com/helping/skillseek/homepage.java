package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.divider.MaterialDivider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class homepage extends AppCompatActivity implements View.OnClickListener {
    CircleImageView profilePic;

    TextView skillseek;
    Vibrator vibrator;
    EditText inputSkill;
    ImageButton skillSearchBtn;
    RecyclerView recyclerView;
    DatabaseReference hireeRef,hirerRef;
    hireeAdapter adapter;
    String gotSkill,uidFirebaseAuth;
    ArrayList<hireeDetailsForFB> list;
    FirebaseAuth auth;
    String imageURL;

    int z=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);


        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));

        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);


        skillseek = findViewById(R.id.textSkillSeek);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        profilePic = findViewById(R.id.profilepicdisplay);
        inputSkill = findViewById(R.id.searchSkill);
        skillSearchBtn = findViewById(R.id.searchButton);
        recyclerView = findViewById(R.id.hireeListDisplay);


        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        adapter = new hireeAdapter(this,list);
        recyclerView.setAdapter(adapter);


        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String profilePicImage = sharedPreferences.getString("imageurl","default");
        String id = sharedPreferences.getString("uniqueID","default");
        String category = sharedPreferences.getString("category","default");

        auth = FirebaseAuth.getInstance();
        if (auth.getCurrentUser()!=null) {
            String authID = auth.getCurrentUser().getUid();
            Log.d("Unique ID",authID+"hvkhkhvkjvhkgvkghvkgvjghvkhgckghckhgckghckghckhgckhcg");
            hirerRef = FirebaseDatabase.getInstance().getReference("hirer");
            hireeRef = FirebaseDatabase.getInstance().getReference("hiree");

            hirerRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.child(authID).exists()) {
                        Log.d("Snapshot data", String.valueOf(snapshot.child(authID)));
                        imageURL = snapshot.child(authID).child("downloadUrl").getValue(String.class);
                        if (imageURL!=null && !imageURL.isEmpty()) {
                            Picasso.get()
                                    .load(imageURL)
                                    .placeholder(R.drawable.profilepicture)
                                    .error(R.drawable.profilepicture)
                                    .into(profilePic);
                        }
                        else {
                            Toast.makeText(homepage.this,"Profile pic image not loaded",Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Log.d("Datasnapshot ","It does not exist");
                        hireeRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if (snapshot.child(authID).exists()) {
                                    imageURL = snapshot.child(authID).child("downloadUrl").getValue(String.class);
                                    if (imageURL!=null && !imageURL.isEmpty()) {
                                        Picasso.get()
                                                .load(imageURL)
                                                .placeholder(R.drawable.profilepicture)
                                                .error(R.drawable.profilepicture)
                                                .into(profilePic);
                                    }
                                    else {
                                        Toast.makeText(homepage.this,"Profile pic image not loaded",Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {

                }
            });
        }



        profilePic.setOnClickListener(this);
        skillSearchBtn.setOnClickListener(this);
        inputSkill.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i== EditorInfo.IME_ACTION_SEARCH){
                    skillSearchBtn.performClick();
                    return true;
                }
                return false;
            }
        });
        z=0;
        performSkillQuery(gotSkill);

    }



    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.profilepicdisplay) {
            vibrator.vibrate(1);
            Intent intent = new Intent(this, view_profile.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.searchButton){
            gotSkill = inputSkill.getText().toString().trim().toLowerCase();
            if (!gotSkill.isEmpty()) {
                z=1;
                performSkillQuery(gotSkill);
            } else {
                Toast.makeText(homepage.this, "Please enter a skill to search", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //This code gets the skills and shows in textview
    private void performSkillQuery(String partialSkill) {
        hireeRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    hireeDetailsForFB hiree = dataSnapshot.getValue(hireeDetailsForFB.class);
                    if (z == 1) {
                        String skill = hiree.getSkill().toLowerCase();
                        if (skill.contains(partialSkill)) {
                            list.add(hiree);
                        }
                    }
                    else {
                        list.add(hiree);
                        System.out.println(hiree.getName());
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("DatabaseError", "Error reading from database: " + error.getMessage());
            }
        });
    }

    @Override
    public void onBackPressed() {
        //This code helps go back to homepage(android home launcher) when back is pressed
        super.onBackPressed();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
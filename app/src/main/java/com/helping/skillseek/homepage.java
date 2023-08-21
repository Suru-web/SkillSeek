package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class homepage extends AppCompatActivity implements View.OnClickListener {
    CircleImageView profilePic;

    TextView skillseek;
    Vibrator vibrator;
    EditText inputSkill;
    ImageButton skillSearchBtn;
    private DatabaseReference hireeRef;

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

        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String profilePicImage = sharedPreferences.getString("imageurl","default");
        String id = sharedPreferences.getString("uniqueID","default");
        if (profilePicImage!=null && !profilePicImage.isEmpty()) {
            Picasso.get()
                    .load(profilePicImage)
                    .placeholder(R.drawable.profilepicture)
                    .error(R.drawable.profilepicture)
                    .into(profilePic);
        }
        else {
            Toast.makeText(this,"Profile pic image not loaded",Toast.LENGTH_SHORT).show();
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        hireeRef = database.getReference("hiree");


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

    }


    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.profilepicdisplay) {
            vibrator.vibrate(1);
            Intent intent = new Intent(this, view_profile.class);
            startActivity(intent);
        }
        else if (v.getId()==R.id.searchButton){
            String gotSkill = inputSkill.getText().toString().trim().toLowerCase();
            Log.d("skill",gotSkill);
            if (!gotSkill.isEmpty()) {
                performSkillQuery(gotSkill);
            } else {
                Toast.makeText(homepage.this, "Please enter a skill to search", Toast.LENGTH_SHORT).show();
            }
        }
    }
    //This code gets the skills and shows in textview
    private void performSkillQuery(String gotSkill) {
        hireeRef.orderByChild("skill").equalTo(gotSkill).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear any previous data or update UI as needed
                // For example, you can display the results in a TextView
                TextView resultTextView = findViewById(R.id.textView8);
                StringBuilder resultBuilder = new StringBuilder();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    hireeDetailsForFB hiree = dataSnapshot.getValue(hireeDetailsForFB.class);
                    String name = hiree.getName();
                    String skill = hiree.getSkill();

                    resultBuilder.append("Name: ").append(name).append("\n");
                    resultBuilder.append("Skill: ").append(skill).append("\n\n");
                }

                resultTextView.setText(resultBuilder.toString());
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
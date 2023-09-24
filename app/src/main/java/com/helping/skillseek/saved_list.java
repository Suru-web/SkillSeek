package com.helping.skillseek;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.helping.skillseek.Adapter.savedAdapter;
import com.helping.skillseek.Objects.hireeDetailsForFB;
import com.helping.skillseek.Objects.savedAccount;

import java.util.ArrayList;

public class saved_list extends AppCompatActivity implements View.OnClickListener {

    RecyclerView recyclerView;
    ImageButton backBTN;
    ArrayList<hireeDetailsForFB> list;
    ArrayList<savedAccount> sList;
    DatabaseReference mainRef,savedREF;
    savedAdapter adapter;
    FirebaseAuth auth;
    String userID;
    Vibrator vibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_list);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));

        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);

        recyclerView = findViewById(R.id.savedListDisplay);
        backBTN = findViewById(R.id.backBtnSaved);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        list = new ArrayList<>();
        sList = new ArrayList<>();
        adapter = new savedAdapter(this,list);
        auth = FirebaseAuth.getInstance();
        userID = auth.getCurrentUser().getUid();
        savedREF = FirebaseDatabase.getInstance().getReference("LIKED").child(userID);
        mainRef = FirebaseDatabase.getInstance().getReference("hiree");
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        ArrayList<String> stringID = new ArrayList<>();
        savedREF.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                stringID.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    savedAccount save = dataSnapshot.getValue(savedAccount.class);
                    if (!save.isLiked()) {
                        // If it's a liked item, add it to the list
                        stringID.add(save.getLikedID());
                    }
                }
                mainRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                            hireeDetailsForFB hiree = dataSnapshot.getValue(hireeDetailsForFB.class);
                            for (int i = 0; i<stringID.size();i++){
                                if (stringID.get(i).equals(hiree.getId())){
                                    list.add(hiree);
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event
            }
        });
        backBTN.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.backBtnSaved){
            vibrator.vibrate(2);
            finish();
        }
    }
}
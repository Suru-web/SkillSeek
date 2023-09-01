package com.helping.skillseek;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_profile extends AppCompatActivity implements View.OnClickListener {
    EditText name,username,age,skill;
    CircleImageView pfp,pfpedit;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    int check;
    String loadImage;
    String id,categ;
    Uri imageUri;
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    TextView tvname,tvuname,tvskill,tvage;
    ImageButton back;
    Vibrator vibrator;
    Button submit;

    String na,una,sk,ag,img,ph,gid,imageURL;
    StorageReference imagereference;
    StorageReference storageReference = FirebaseStorage.getInstance().getReference();
    StorageReference profilePicsRef;

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
        tvage = findViewById(R.id.tvAge);
        tvname = findViewById(R.id.tvName);
        tvuname = findViewById(R.id.tvuname);
        tvskill = findViewById(R.id.tvSkill);
        submit = findViewById(R.id.submitEP);
        back = findViewById(R.id.backBtnEP);
        pfpedit = findViewById(R.id.pfpeditInEP);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        auth = FirebaseAuth.getInstance();
        id = auth.getCurrentUser().getUid();
        Intent intent = getIntent();
        categ = intent.getStringExtra("editProfileGetCategory");
        Log.d("Category in EP",categ);


        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), selectedUri -> {
            if (selectedUri != null) {
                Log.d("PhotoPicker", "Selected URI: " + selectedUri);
                pfp.setImageURI(selectedUri);
                imageUri = selectedUri;
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });


        if (categ.equals("Hirer")||categ.equals("hirer")) {
            databaseReference = FirebaseDatabase.getInstance().getReference("hirer").child(id);
            profilePicsRef = storageReference.child("hirer_profile_pictures");
            check = 1;
        }
        else if (categ.equals("hiree")||categ.equals("Hiree")){
            databaseReference = FirebaseDatabase.getInstance().getReference("hiree").child(id);
            profilePicsRef = storageReference.child("hiree_profile_pictures");
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
                    tvname.setText("Name");
                    tvuname.setText("Username");
                    tvskill.setText("Email");
                    tvage.setText("Address");
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
                    tvname.setText("Name");
                    tvuname.setText("Username");
                    tvskill.setText("Skill");
                    tvage.setText("Age");
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
        submit.setOnClickListener(this);
        back.setOnClickListener(this);
        pfpedit.setOnClickListener(this);
        pfp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v.getId()==R.id.submitEP){
            vibrator.vibrate(2);
            String Name, userName, Age, SKILL,ID,IMAGEURL;
            Name = name.getText().toString();
            userName = username.getText().toString();
            Age = age.getText().toString();
            SKILL = skill.getText().toString();

            if (check == 0) {
                imagereference = profilePicsRef.child(auth.getCurrentUser().getUid());
                if (imageUri!=null) {
                    UploadTask uploadTask = imagereference.putFile(imageUri);
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> downloadUrlTask = imagereference.getDownloadUrl();
                        downloadUrlTask.addOnSuccessListener(uri1 -> {
                            imageURL = uri1.toString();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("name", Name);
                            hashMap.put("username", userName);
                            hashMap.put("age", Age);
                            hashMap.put("skill", SKILL);
                            hashMap.put("phoneNumber", auth.getCurrentUser().getPhoneNumber());
                            hashMap.put("id", auth.getCurrentUser().getUid());
                            hashMap.put("downloadUrl", imageURL);
                            Log.d("Image URL", imageURL);databaseReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@androidx.annotation.NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        });
                    }).addOnFailureListener(exception -> {
                        Toast.makeText(this, "Image not uploaded", Toast.LENGTH_SHORT).show();
                    });
                }
            } else if (check == 1) {
                imagereference = profilePicsRef.child(auth.getCurrentUser().getUid());
                if (imageUri!=null) {
                    UploadTask uploadTask = imagereference.putFile(imageUri);
                    uploadTask.addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> downloadUrlTask = imagereference.getDownloadUrl();
                        downloadUrlTask.addOnSuccessListener(uri1 -> {
                            imageURL = uri1.toString();
                            HashMap<String, Object> hashMap = new HashMap<>();
                            hashMap.put("name", Name);
                            hashMap.put("username", userName);
                            hashMap.put("address", Age);
                            hashMap.put("email", SKILL);
                            hashMap.put("phoneNumber",auth.getCurrentUser().getPhoneNumber());
                            hashMap.put("id",auth.getCurrentUser().getUid());
                            hashMap.put("downloadUrl",imageURL);
                            Log.d("Image URL",imageURL);
                            databaseReference.setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(edit_profile.this, "Updated profile", Toast.LENGTH_SHORT).show();
                                }

                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@androidx.annotation.NonNull Exception e) {
                                    e.printStackTrace();
                                }
                            });
                        });
                    }).addOnFailureListener(exception -> {
                        Toast.makeText(this,"Image not uploaded",Toast.LENGTH_SHORT).show();
                    });
                }

            }finish();
        } else if (v.getId()==R.id.backBtnEP) {
            vibrator.vibrate(2);
            finish();
        } else if (v.getId()==R.id.pfpeditInEP) {
            vibrator.vibrate(2);
            getImage();
        } else if (v.getId()==R.id.pfpInEP) {
            showImagePopup(imageUri);
        }
    }
    public void getImage(){
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
    private void showImagePopup(Uri uri) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);


        ImageView imageView = new ImageView(this);
        imageView.setImageURI(uri);
        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.addContentView(imageView, new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT));

        dialog.show();
    }
}
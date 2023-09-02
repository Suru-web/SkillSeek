package com.helping.skillseek;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class Hiree_main extends AppCompatActivity implements View.OnClickListener {

    String[] skills = {"Plumber", "Carpenter", "Painter", "Gardener", "House Cleaning", "Masseuse", "Cook", "Write your own"};
    String item,custSkill,downloadUrl,uID;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterSkills;
    TextInputLayout customskill, hireedropd, hireeName, hireeUserName, hireeAge;
    Button submit;
    ImageButton hireePutPic;
    String bool = "false",phNum;
    String skill;
    CircleImageView hireeProfilePicture;
    Uri uri;
    Vibrator vibrator;
    int a=0,net;
    String authUID;
    DatabaseReference databasehiree;
    TextView pfperror;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference profilePicsRef = storageReference.child("hiree_profile_pictures");
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    Uri imageUri;
    String imageDownloadUrl;
    StorageReference imagereference;
    String phoneNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiree_main);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.defPurp));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));


        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        phoneNum = user.getPhoneNumber();
        authUID = auth.getUid();


        customskill = findViewById(R.id.hireeCustomSkill);
        hireedropd = findViewById(R.id.hireeDropD);
        hireeName = findViewById(R.id.hireeNameLayout);
        hireeUserName = findViewById(R.id.hireeUserNameLayout);
        hireePutPic = findViewById(R.id.hireeProfilePictureEditBtn);
        hireeProfilePicture = findViewById(R.id.hireeProfilePicture);
        hireeProfilePicture.setImageResource(R.drawable.profilepicture);
        pfperror = findViewById(R.id.pfperrorshowtv);
        hireeAge = findViewById(R.id.hireeAge);
        submit = findViewById(R.id.hireeSubmitBtn);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        databasehiree = FirebaseDatabase.getInstance().getReference("hiree");

        pickMedia = registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), selectedUri -> {
            if (selectedUri != null) {
                Log.d("PhotoPicker", "Selected URI: " + selectedUri);
                hireeProfilePicture.setImageURI(selectedUri);
                imageUri = selectedUri;
            } else {
                Log.d("PhotoPicker", "No media selected");
            }
        });


        if (isInternetAvailable()) {
            net = 1;
        } else {
            net = 0;
            Intent intent = new Intent(Hiree_main.this,internetnotavailable.class);
            startActivity(intent);
        }


        autoCompleteTextView = findViewById(R.id.dropDownAutoComplete);
        adapterSkills = new ArrayAdapter<String>(this, R.layout.hiree_skill_dropdown, skills);
        autoCompleteTextView.setAdapter(adapterSkills);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vibrator.vibrate(1);
                item = adapterView.getItemAtPosition(i).toString();
                custSkill = item.toLowerCase();
                skill = custSkill;
                if (item.equals("Write your own")) {
                    customskill.setVisibility(View.VISIBLE);
                    hireedropd.setVisibility(View.GONE);
                    a=1;
                }
            }
        });

        submit.setOnClickListener(this);
        hireeProfilePicture.setOnClickListener(this);
        hireePutPic.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.hireeSubmitBtn){
            vibrator.vibrate(5);
            String name, uname, age;
            name = hireeName.getEditText().getText().toString();
            uname = hireeUserName.getEditText().getText().toString();
            age = hireeAge.getEditText().getText().toString();
            if (a==1){
                custSkill = customskill.getEditText().getText().toString();
                skill = custSkill.toLowerCase();
            }
            vibrator.vibrate(5);
            if (name.isEmpty()) {
                hireeName.setHelperText("Name cannot be empty");
                Toast.makeText(Hiree_main.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
            } else if (uname.isEmpty()) {
                hireeUserName.setHelperText("UserName cannot be empty");
                Toast.makeText(Hiree_main.this, "UserName cannot be empty", Toast.LENGTH_LONG).show();
            } else if (age.isEmpty()) {
                hireeAge.setHelperText("Age cannot be empty");
                Toast.makeText(Hiree_main.this, "Age cannot be empty", Toast.LENGTH_LONG).show();
            } else if (!age.isEmpty()) {
                if (Integer.parseInt(age) < 20) {
                    hireeAge.setHelperText("People below age 20 are not allowed");
                    Toast.makeText(Hiree_main.this, "People below 20 age not allowed", Toast.LENGTH_LONG).show();
                } else if (Integer.parseInt(age) > 100) {
                    hireeAge.setHelperText("Input valid Age");
                    Toast.makeText(Hiree_main.this, "Please Select valid age", Toast.LENGTH_LONG).show();
                } else {
                    bool = "true";
                    if (net == 1) {
                        skipToParentElse:
                        {
                            if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                                Toast.makeText(Hiree_main.this,"Location access already granted",Toast.LENGTH_SHORT);
                            }
                            else {
                                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
                            }
                            imagereference = profilePicsRef.child(authUID);
                            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("data", "true");
                            if (imageUri!=null) {
                                UploadTask uploadTask = imagereference.putFile(imageUri);
                                uploadTask.addOnSuccessListener(taskSnapshot -> {
                                    Task<Uri> downloadUrlTask = imagereference.getDownloadUrl();
                                    downloadUrlTask.addOnSuccessListener(uri1 -> {
                                        downloadUrl = uri1.toString();
                                        hireeDetails hiree = new hireeDetails(authUID, name, uname, skill, age,downloadUrl,phoneNum);
                                        databasehiree.child(authUID).setValue(hiree);

                                        Intent intent = new Intent(this, homepage.class);
                                        editor.putString("imageurl",downloadUrl);
                                        editor.putString("uniqueID",authUID);
                                        editor.putString("category","Hiree");
                                        editor.apply();
                                        startActivity(intent);
                                    });
                                }).addOnFailureListener(exception -> {
                                    Toast.makeText(this,"Image not uploaded",Toast.LENGTH_SHORT).show();
                                });
                            }
                            else {
                                pfperror.setVisibility(View.VISIBLE);
                            }
                            break skipToParentElse;
                        }
                    }
                }
            }
        }
        else if (view.getId()==R.id.hireeProfilePictureEditBtn){
            getImage();
        }
        else if (view.getId()==R.id.hireeProfilePicture){
            showImagePopup(uri);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInternetAvailable()) {
            net = 1;
        } else {
            net = 0;
            Intent intent = new Intent(Hiree_main.this,internetnotavailable.class);
            startActivity(intent);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String fetchedCategory = sharedPreferences.getString("data","default");
        if (fetchedCategory.equals("true")){
            Intent intent = new Intent(this, homepage.class);
            startActivity(intent);
            finish();
        }
    }
    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            return activeNetworkInfo != null && activeNetworkInfo.isConnected();
        }
        return false;
    }
    public void getImage(){
        pickMedia.launch(new PickVisualMediaRequest.Builder()
                .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                .build());
    }
    private void showImagePopup(Uri uri) {
        final Dialog dialog = new Dialog(this, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
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
    public void onRequestPermissionResult(int requestCode, String[] permissions, int [] grantResults){
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(Hiree_main.this,"Location access granted now",Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
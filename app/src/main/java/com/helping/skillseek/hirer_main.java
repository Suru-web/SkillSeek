package com.helping.skillseek;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class hirer_main extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout hirername,hireruname,hireremail,hireraddress;
    Button submit;
    ImageButton hirerPutPic;
    CircleImageView hirerProfilePicture;
    Vibrator vibrator;
    DatabaseReference databasehirer;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    StorageReference profilePicsRef = storageReference.child("hirer_profile_pictures");
    private ActivityResultLauncher<PickVisualMediaRequest> pickMedia;
    Uri uri;
    int net;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hirer_main);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.bluePurp));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));

        hireraddress = findViewById(R.id.hirerAddress);
        hireremail = findViewById(R.id.hirerEmail);
        hirername = findViewById(R.id.hirerNameLayout);
        hireruname = findViewById(R.id.hirerUserNameLayout);
        hirerPutPic = findViewById(R.id.hirerProfilePictureEditBtn);
        hirerProfilePicture = findViewById(R.id.hirerProfilePicture);
        hirerProfilePicture.setImageResource(R.drawable.profilepicture);
        submit = findViewById(R.id.hirerSubmitBtn);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        databasehirer = FirebaseDatabase.getInstance().getReference("hirer");//Sets the path to hirer


        //code for selecting image from gallery
        pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), selectedUri -> {
                    if (selectedUri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + selectedUri);
                        hirerProfilePicture.setImageURI(selectedUri);
                        uri = selectedUri;
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });


        if (isInternetAvailable()) {
            net = 1;
        } else {
            net = 0;
            Intent intent = new Intent(hirer_main.this,internetnotavailable.class);
            startActivity(intent);
        }

        submit.setOnClickListener(this);
        hirerPutPic.setOnClickListener(this);
        hirerProfilePicture.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId()==R.id.hirerProfilePictureEditBtn){
            getImage();
            Toast.makeText(this,"upload pic",Toast.LENGTH_SHORT).show();
        }
        else if (view.getId() == R.id.hirerProfilePicture){
            showImagePopup(uri);
        }
        else if (view.getId()== R.id.hirerSubmitBtn){
            vibrator.vibrate(5);
            String name,email,uname,address;
            name = hirername.getEditText().getText().toString();
            email = hireremail.getEditText().getText().toString();
            uname = hireruname.getEditText().getText().toString();
            address = hireraddress.getEditText().getText().toString();
            String bool = "false";

            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(hirer_main.this,"Location access already granted",Toast.LENGTH_LONG);
            }
            else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
            }


            if (name.isEmpty()){
                bool = "false";
                Toast.makeText(hirer_main.this,"Name cannot be empty",Toast.LENGTH_LONG).show();
            } else if (email.isEmpty()) {
                bool = "false";
                Toast.makeText(hirer_main.this,"Email cannot be empty",Toast.LENGTH_LONG).show();
            } else if (uname.isEmpty()) {
                bool = "false";
                Toast.makeText(hirer_main.this,"UserName cannot be empty",Toast.LENGTH_LONG).show();
            } else if (address.isEmpty()) {
                bool = "false";
                Toast.makeText(hirer_main.this,"Address cannot be empty",Toast.LENGTH_LONG).show();
            } else if (!email.contains("@")||!email.contains(".com")) {
                bool = "false";
                Toast.makeText(hirer_main.this,"Email Address is not valid",Toast.LENGTH_LONG).show();
            }
            else {
                bool = "true";
                if (net == 1) {
                    if (bool.equals("true")) {
                        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("data", "true");
                        editor.apply();

                        //Adds data to firebase
                        String id = databasehirer.push().getKey();
                        hirerDetails hirer = new hirerDetails(id, name, uname, email, address);
                        databasehirer.child(id).setValue(hirer);


                        Toast.makeText(hirer_main.this, "Data entered successfully", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(this, homepage.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(hirer_main.this, "Error", Toast.LENGTH_LONG).show();
                    }
                }
            }
        }

    }


    public void onRequestPermissionResult(int requestCode, String[] permissions, int [] grantResults){
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(hirer_main.this,"Location access granted now",Toast.LENGTH_LONG);
            } else {
                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isInternetAvailable()) {
            net = 1;
        } else {
            net = 0;
            Intent intent = new Intent(hirer_main.this,internetnotavailable.class);
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
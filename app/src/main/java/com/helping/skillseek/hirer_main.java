package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.Manifest;
import android.content.pm.PackageManager;

import com.google.android.material.textfield.TextInputLayout;

public class hirer_main extends AppCompatActivity implements View.OnClickListener {

    TextInputLayout hirername,hireruname,hireremail,hireraddress;
    Button submit;
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
        submit = findViewById(R.id.hirerSubmitBtn);

        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
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
            if (bool.equals("true")) {
                SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("data","true");
                editor.apply();
                Toast.makeText(hirer_main.this, "Data entered successfully", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(this, homepage.class);
                startActivity(intent);
            }
            else{
                Toast.makeText(hirer_main.this,"Error in sharedp",Toast.LENGTH_LONG).show();
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
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
        String fetchedCategory = sharedPreferences.getString("data","default");
        if (fetchedCategory.equals("true")){
            Intent intent = new Intent(this, homepage.class);
            startActivity(intent);
        }
    }
}
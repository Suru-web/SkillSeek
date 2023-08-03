package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

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

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(hirer_main.this,"Location access already granted",Toast.LENGTH_LONG);
        }
        else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }

        if (name.isEmpty()){
            Toast.makeText(hirer_main.this,"Name cannot be empty",Toast.LENGTH_LONG).show();
        } else if (email.isEmpty()) {
            Toast.makeText(hirer_main.this,"Email cannot be empty",Toast.LENGTH_LONG).show();
        } else if (uname.isEmpty()) {
            Toast.makeText(hirer_main.this,"UserName cannot be empty",Toast.LENGTH_LONG).show();
        } else if (address.isEmpty()) {
            Toast.makeText(hirer_main.this,"Address cannot be empty",Toast.LENGTH_LONG).show();
        } else if (!email.contains("@")||!email.contains(".com")) {
            Toast.makeText(hirer_main.this,"Email Address is not valid",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(hirer_main.this,"Data entered successfully",Toast.LENGTH_LONG).show();
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
}
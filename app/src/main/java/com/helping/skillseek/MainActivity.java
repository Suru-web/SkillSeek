package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button contBtn;
    TextInputLayout phno, pass;
    TextView lgBtn;
    String mobileNumber;
    String Password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //This code sets the status bar color
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));

        //This part of the code displays the statusbar icon color to black
        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);


        contBtn = findViewById(R.id.continueButton);
        phno = findViewById(R.id.phNumberInput);
        pass = findViewById(R.id.passwordInput);
        lgBtn = findViewById(R.id.LoginButton);

        contBtn.setOnClickListener(this);
        lgBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.continueButton){
            mobileNumber = phno.getEditText().getText().toString();
            Password = pass.getEditText().getText().toString();
            if (mobileNumber.isEmpty()){
                Toast toast = Toast.makeText(this,"Enter the Phone number",Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (mobileNumber.length()<10){
                Toast toast = Toast.makeText(this,"Enter 10 digits phone number properly",Toast.LENGTH_LONG);
                toast.show();
            } else if (Password.isEmpty()) {
                Toast toast = Toast.makeText(this,"Enter Password",Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (Password.length()<8){
                Toast toast = Toast.makeText(this,"Minimum 8 characters password is required",Toast.LENGTH_LONG);
                toast.show();
            }
            else {
                Intent intent = new Intent(this, loginPage.class);
                startActivity(intent);
            }
        }
        else if (view.getId() == R.id.LoginButton){
            Intent intent = new Intent(this, loginPage.class);
            startActivity(intent);
        }
    }
}
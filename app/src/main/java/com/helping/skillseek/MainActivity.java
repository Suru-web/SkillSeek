package com.helping.skillseek;

import static android.widget.Toast.LENGTH_LONG;
import static android.widget.Toast.makeText;

import android.content.Context;
import android.os.Vibrator;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.ktx.Firebase;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button contBtn, getotpbtn;
    TextInputLayout phno, pass;
    TextView lgBtn;
    String mobileNumber;
    String Password;
    ProgressBar pgb;
    LinearLayout lockimgLl, passLL;
    String otpVerify;
    Vibrator vibrator;
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
        getotpbtn = findViewById(R.id.getOTPBtn);
        phno = findViewById(R.id.phNumberInput);
        pass = findViewById(R.id.passwordInput);
        pgb = findViewById(R.id.progress_circular);
        passLL = findViewById(R.id.pas);
        lockimgLl = findViewById(R.id.lockImgLL);
        contBtn.setOnClickListener(this);
        getotpbtn.setOnClickListener(this);
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.continueButton){
            mobileNumber = phno.getEditText().getText().toString();
            Password = pass.getEditText().getText().toString();
            pgb.setVisibility(View.VISIBLE);
            contBtn.setVisibility(View.INVISIBLE);
            vibrator.vibrate(5);
            if (!otpVerify.isEmpty()){
                PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(otpVerify, Password);
                FirebaseAuth.getInstance().signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                         if (task.isComplete()){
                             Toast toast = Toast.makeText(MainActivity.this,"Otp Successfully Verified", Toast.LENGTH_SHORT);
                             toast.show();
                             contBtn.setVisibility(View.VISIBLE);
                             Intent intent = new Intent(MainActivity.this, CategorySelect.class);
                             startActivity(intent);
                         }
                         else {
                             Toast toast = Toast.makeText(MainActivity.this,"Otp verification failed", Toast.LENGTH_SHORT);
                             toast.show();
                         }
                    }
                });
            }
        }
        else if (view.getId() == R.id.getOTPBtn) {
            mobileNumber = phno.getEditText().getText().toString();
            Password = pass.getEditText().getText().toString();
            vibrator.vibrate(5);
            if (mobileNumber.isEmpty()){
                Toast toast = Toast.makeText(this,"Enter the Phone number",Toast.LENGTH_SHORT);
                toast.show();
            }
            else if (mobileNumber.length()<10){
                Toast toast = Toast.makeText(this,"Enter 10 digits phone number properly", LENGTH_LONG);
                toast.show();
            }
            else {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + mobileNumber,
                        60,
                        TimeUnit.SECONDS,
                        MainActivity.this,
                        new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                getotpbtn.setVisibility(View.INVISIBLE);
                                lockimgLl.setVisibility(View.VISIBLE);
                                passLL.setVisibility(View.VISIBLE);
                                contBtn.setVisibility(View.VISIBLE);
                            }

                            @Override
                            public void onVerificationFailed(@NonNull FirebaseException e) {
                                getotpbtn.setVisibility(View.VISIBLE);
                                lockimgLl.setVisibility(View.INVISIBLE);
                                passLL.setVisibility(View.INVISIBLE);
                                contBtn.setVisibility(View.INVISIBLE);
                                Toast toast = Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG);
                                toast.show();
                            }

                            @Override
                            public void onCodeSent(@NonNull String backendotpsent, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                getotpbtn.setVisibility(View.INVISIBLE);
                                lockimgLl.setVisibility(View.VISIBLE);
                                passLL.setVisibility(View.VISIBLE);
                                contBtn.setVisibility(View.VISIBLE);
                                otpVerify = backendotpsent;
                            }
                        }
                );
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser mFBUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mFBUser!= null){
            Intent intent = new Intent(MainActivity.this, CategorySelect.class);
            startActivity(intent);
        }
    }
}
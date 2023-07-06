package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

public class loginPage extends AppCompatActivity implements View.OnClickListener {
    TextView regBtn;
    TextInputLayout phnm,passd;
    Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        //This sets the background to white
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.white));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));

        //This part of the code displays the statusbar icon color to black
        int flags = window.getDecorView().getSystemUiVisibility();
        flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
        window.getDecorView().setSystemUiVisibility(flags);

        phnm = findViewById(R.id.phInput);
        passd = findViewById(R.id.passBoxLogin);
        regBtn = findViewById(R.id.register_Text_Btn);

        regBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.register_Text_Btn){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }
}
package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

public class hireeInfoShow extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiree_info_show);


        Intent intent = getIntent();
        String id =  intent.getStringExtra("hireeListId");
        Toast.makeText(this,id,Toast.LENGTH_LONG).show();
    }
}
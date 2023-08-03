package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

public class Hiree_main extends AppCompatActivity {

    String [] skills = {"Plumber","Carpainter","Painter","Garderner","House Cleaning"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterSkills;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiree_main);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.bluePurp));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));

        autoCompleteTextView = findViewById(R.id.dropDownAutoComplete);
        adapterSkills = new ArrayAdapter<String>(this,R.layout.hiree_skill_dropdown,skills);
        autoCompleteTextView.setAdapter(adapterSkills);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Hiree_main.this,"Item "+item, Toast.LENGTH_LONG).show();
            }
        });
    }
}
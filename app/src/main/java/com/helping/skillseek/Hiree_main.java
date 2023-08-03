package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class Hiree_main extends AppCompatActivity implements View.OnClickListener {

    String [] skills = {"Plumber","Carpenter","Painter","Gardener","House Cleaning","Masseuse","Cook","Select your own"};
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterSkills;
    TextInputLayout customskill,hireedropd,hireeName,hireeUserName,hireeAge;
    Button submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hiree_main);

        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.bluePurp));
        window.setNavigationBarColor(this.getResources().getColor(R.color.white));

        customskill = findViewById(R.id.hireeCustomSkill);
        hireedropd = findViewById(R.id.hireeDropD);
        hireeName = findViewById(R.id.hireeNameLayout);
        hireeUserName = findViewById(R.id.hireeUserNameLayout);
        hireeAge = findViewById(R.id.hireeAge);
        submit = findViewById(R.id.hireeSubmitBtn);

        autoCompleteTextView = findViewById(R.id.dropDownAutoComplete);
        adapterSkills = new ArrayAdapter<String>(this,R.layout.hiree_skill_dropdown,skills);
        autoCompleteTextView.setAdapter(adapterSkills);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
//                Toast.makeText(Hiree_main.this,"Item "+item, Toast.LENGTH_LONG).show();
                if (item.equals("Select your own")){
                    customskill.setVisibility(View.VISIBLE);
                    hireedropd.setVisibility(View.GONE);
                }
            }
        });

        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String name,uname,age;
        name = hireeName.getEditText().getText().toString();
        uname = hireeUserName.getEditText().getText().toString();
        age = hireeAge.getEditText().getText().toString();
        if (name.isEmpty()){
            Toast.makeText(Hiree_main.this,"Name cannot be empty",Toast.LENGTH_LONG).show();
        }
        else if (uname.isEmpty()){
            Toast.makeText(Hiree_main.this,"UserName cannot be empty",Toast.LENGTH_LONG).show();
        } else if (age.isEmpty()) {
            Toast.makeText(Hiree_main.this,"Age cannot be empty",Toast.LENGTH_LONG).show();
        }
        else if (!age.isEmpty()){
            if (Integer.parseInt(age)<=20){
                Toast.makeText(Hiree_main.this,"People below 20 age not allowed",Toast.LENGTH_LONG).show();
            }
            else if (Integer.parseInt(age)>100){
                Toast.makeText(Hiree_main.this,"Please Select valid age",Toast.LENGTH_LONG).show();
            }
            else {
                skipToParentElse:
                {
                    Toast.makeText(Hiree_main.this, "Data entry successful", Toast.LENGTH_LONG).show();
                    break skipToParentElse;
                }
            }
        }
        else {
            Toast.makeText(Hiree_main.this,"Data entry successfull",Toast.LENGTH_LONG).show();
        }
    }
}
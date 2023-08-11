package com.helping.skillseek;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Hiree_main extends AppCompatActivity implements View.OnClickListener {

    String[] skills = {"Plumber", "Carpenter", "Painter", "Gardener", "House Cleaning", "Masseuse", "Cook", "Select your own"};
    String item;
    AutoCompleteTextView autoCompleteTextView;
    ArrayAdapter<String> adapterSkills;
    TextInputLayout customskill, hireedropd, hireeName, hireeUserName, hireeAge;
    Button submit;
    String bool = "false";
    Vibrator vibrator;
    DatabaseReference databasehiree;

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
        vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        databasehiree = FirebaseDatabase.getInstance().getReference("hiree");

        autoCompleteTextView = findViewById(R.id.dropDownAutoComplete);
        adapterSkills = new ArrayAdapter<String>(this, R.layout.hiree_skill_dropdown, skills);
        autoCompleteTextView.setAdapter(adapterSkills);
        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vibrator.vibrate(1);
                item = adapterView.getItemAtPosition(i).toString();
                Toast.makeText(Hiree_main.this,"Item "+item, Toast.LENGTH_LONG).show();
                if (item.equals("Write your own")) {
                    customskill.setVisibility(View.VISIBLE);
                    hireedropd.setVisibility(View.GONE);
                    item = customskill.getEditText().getText().toString();
                }
            }
        });

        submit.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String name, uname, age;
        name = hireeName.getEditText().getText().toString();
        uname = hireeUserName.getEditText().getText().toString();
        age = hireeAge.getEditText().getText().toString();
        vibrator.vibrate(5);
        if (name.isEmpty()) {
            Toast.makeText(Hiree_main.this, "Name cannot be empty", Toast.LENGTH_LONG).show();
        } else if (uname.isEmpty()) {
            Toast.makeText(Hiree_main.this, "UserName cannot be empty", Toast.LENGTH_LONG).show();
        } else if (age.isEmpty()) {
            Toast.makeText(Hiree_main.this, "Age cannot be empty", Toast.LENGTH_LONG).show();
        } else if (!age.isEmpty()) {
            if (Integer.parseInt(age) <= 20) {
                Toast.makeText(Hiree_main.this, "People below 20 age not allowed", Toast.LENGTH_LONG).show();
            } else if (Integer.parseInt(age) > 100) {
                Toast.makeText(Hiree_main.this, "Please Select valid age", Toast.LENGTH_LONG).show();
            } else {
                skipToParentElse:
                {
                    bool = "true";
                    SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("data","true");
                    editor.apply();


                    String id = databasehiree.push().getKey();
                    hireeDetails hiree = new hireeDetails(id,name,uname,item,age);
                    databasehiree.child(id).setValue(hiree);


                    Toast.makeText(Hiree_main.this, "Data entry successful", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(this, homepage.class);
                    startActivity(intent);
                    break skipToParentElse;
                }
            }
        } else {
            bool = "true";
            SharedPreferences sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("data","true");
            editor.apply();
            Toast.makeText(Hiree_main.this, "Data entry successful", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, homepage.class);
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
}
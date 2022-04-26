package com.sujan.trackingme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static com.sujan.trackingme.R.*;


public class RegisterActivity extends AppCompatActivity {

    EditText username,password,email,country,dob;
    RadioGroup gender;
    DatabaseHelper databaseHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layout.activity_register);

        username = findViewById(id.usernamereg);
        password = findViewById(id.passwordreg);
        email = findViewById(id.emailreg);
        dob = findViewById(id.dobreg);
        country = findViewById(id.countryreg);
        gender = findViewById(id.gender);
        databaseHelper = new DatabaseHelper(this);
    }

    public void callLoginform(View view) {
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public void registrationFunc(View view) {
        //code for registration
        String usernameValue = username.getText().toString();
        String passwordValue = password.getText().toString();
        String emailValue = email.getText().toString();
        String dobValue = dob.getText().toString();
        String countryValue = country.getText().toString();
        RadioButton checkedBtn = findViewById(gender.getCheckedRadioButtonId());
        String genderValue = checkedBtn.getText().toString();

        if(usernameValue.isEmpty()) {
            Toast.makeText(RegisterActivity.this, "Enter username", Toast.LENGTH_LONG).show();
        }else if(passwordValue.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Enter Password", Toast.LENGTH_LONG).show();
        }
        else if(emailValue.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Enter Email", Toast.LENGTH_LONG).show();
        }
        else if(dobValue.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Enter Date of birth", Toast.LENGTH_LONG).show();
        }
        else if(countryValue.isEmpty()){
            Toast.makeText(RegisterActivity.this, "Enter Country name", Toast.LENGTH_LONG).show();
        }
        else{
            ContentValues contentValues = new ContentValues();
            contentValues.put("username",usernameValue);
            contentValues.put("password",passwordValue);
            contentValues.put("email",emailValue);
            contentValues.put("dob",dobValue);
            contentValues.put("country",countryValue);
            contentValues.put("gender",genderValue);
            databaseHelper.insertUser(contentValues);

            Toast.makeText(RegisterActivity.this,"Registration is success",Toast.LENGTH_SHORT).show();
//            Intent intent =new Intent(RegisterActivity.this,LoginActivity.class);
//            startActivity(intent);
        }
    }
}
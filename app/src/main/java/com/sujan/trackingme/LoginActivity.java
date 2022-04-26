package com.sujan.trackingme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;



public class LoginActivity extends AppCompatActivity {

    EditText username,password;
    DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.logusername);
        password = findViewById(R.id.logpassword);
        databaseHelper = new DatabaseHelper(this);
    }

    public void callregform(View view) {
        Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
        startActivity(intent);
    }

    public void loginFunction(View view) {
        //code for login checking...
        String usernameValue = username.getText().toString();
        String passwordValue = password.getText().toString();

        if(databaseHelper.isLoginValid(usernameValue,passwordValue)){
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
            Toast.makeText(LoginActivity.this,"Login is Successfull..!",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(LoginActivity.this,"Invalid user name or password..!",Toast.LENGTH_LONG).show();
        }

    }
}
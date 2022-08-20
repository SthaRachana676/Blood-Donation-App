package com.example.androidbloodbank;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

public class ChangePasswordActivity extends AppCompatActivity {
Button btn_change_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

   btn_change_password = findViewById(R.id.changebutton);
    }
}
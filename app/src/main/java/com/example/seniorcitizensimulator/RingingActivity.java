package com.example.seniorcitizensimulator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class RingingActivity extends AppCompatActivity {
    TextView tvName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ringing);
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        tvName=findViewById(R.id.tvName);
        tvName.setText(name);
    }
}
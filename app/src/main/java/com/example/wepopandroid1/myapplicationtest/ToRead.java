package com.example.wepopandroid1.myapplicationtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

public class ToRead extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_read);
        Toast.makeText(ToRead.this,"entering",Toast.LENGTH_LONG).show();
    }
}

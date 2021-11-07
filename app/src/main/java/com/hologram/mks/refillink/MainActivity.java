package com.hologram.mks.refillink;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button scanBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        scanBtn = findViewById(R.id.scanBtn);

        Intent daftar = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(daftar);

        scanBtn.setOnClickListener(v -> {
            Intent scanQr = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(scanQr);
        });
    }
}
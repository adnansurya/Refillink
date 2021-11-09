package com.hologram.mks.refillink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    Button scanBtn;
    TextView namaTxt, emailTxt;
    FirebaseAuth auth;
    FirebaseUser user;

    String name, email, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        namaTxt = findViewById(R.id.namaTxt);
        emailTxt = findViewById(R.id.email2Txt);
        scanBtn = findViewById(R.id.scanBtn);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();
            namaTxt.setText(name);
            emailTxt.setText(email);
            // User is signed in
//            Toast.makeText(this, "ADA " + name, Toast.LENGTH_SHORT).show();
        } else {
            // No user is signed in
            Toast.makeText(this, "Silahkan Login", Toast.LENGTH_SHORT).show();
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
        }

        scanBtn.setOnClickListener(v -> {
            Intent scanQr = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(scanQr);
        });


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.action,menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_scan:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent scan  = new Intent(this, ScanActivity.class);
                startActivity(scan);
                return true;
            case R.id.action_histori:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                Intent histori  = new Intent(this, HistoriActivity.class);
                startActivity(histori);
                return true;
            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...
                auth.signOut();
                Intent logout  = new Intent(this, LoginActivity.class);
                startActivity(logout);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
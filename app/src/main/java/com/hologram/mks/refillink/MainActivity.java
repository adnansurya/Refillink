package com.hologram.mks.refillink;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    Button scanBtn;
    TextView namaTxt, emailTxt, saldoTxt;
    FirebaseAuth auth;
    FirebaseUser user;

    String name, email, uid;
    DatabaseReference mesinRef, userRef;
    long saldo;

    private RecyclerView recyclerView;
    mesinAdapter adapter; // Create Object of the Adapter class


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");
        mesinRef = database.getReference("mesin");

        namaTxt = findViewById(R.id.namaTxt);
        emailTxt = findViewById(R.id.email2Txt);
        scanBtn = findViewById(R.id.scanBtn);
        saldoTxt = findViewById(R.id.saldoTxt);

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

        if(uid != null){
            userRef.child(uid).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    saldo = dataSnapshot.child("saldo").getValue(Long.class);
                    saldoTxt.setText("Rp. " + saldo);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }



        recyclerView = findViewById(R.id.recycler2);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<mesin> options
                = new FirebaseRecyclerOptions.Builder<mesin>()
                .setQuery(mesinRef, new SnapshotParser<mesin>() {
                    @NonNull
                    @Override
                    public mesin parseSnapshot(@NonNull DataSnapshot snapshot) {
                        mesin mesinNew = new mesin();
                        mesinNew.setKapasitas(snapshot.child("kapasitas").getValue(Double.class));
                        mesinNew.setTersedia(snapshot.child("tersedia").getValue(Double.class));
                        mesinNew.setIdMesin(snapshot.getKey());
                        mesinNew.setLokasi(snapshot.child("lokasi").getValue(String.class));
                        mesinNew.setRelay(snapshot.child("relay").getValue(String.class));
                        return mesinNew;
                    }
                })
                .build();


        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new mesinAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);

        scanBtn.setOnClickListener(v -> {
            Intent scanQr = new Intent(MainActivity.this, ScanActivity.class);
            startActivity(scanQr);
        });


    }
    @Override protected void onStop()
    {
        super.onStop();
        adapter.stopListening();
    }


    // Function to tell the app to start getting
    // data from database on starting of the activity
    @Override protected void onStart()
    {
        super.onStart();
        adapter.startListening();

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
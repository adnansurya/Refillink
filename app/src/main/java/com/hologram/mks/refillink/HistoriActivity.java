package com.hologram.mks.refillink;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class HistoriActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    historiAdapter adapter; // Create Object of the Adapter class
    DatabaseReference mbase;

    FirebaseAuth auth;
    FirebaseUser user;

    String name, email, uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_histori);

        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {
            name = user.getDisplayName();
            email = user.getEmail();
            uid = user.getUid();

            // User is signed in
//            Toast.makeText(this, "ADA " + name, Toast.LENGTH_SHORT).show();
        } else {
            // No user is signed in
            Toast.makeText(this, "Silahkan Login", Toast.LENGTH_SHORT).show();
            Intent login = new Intent(HistoriActivity.this, LoginActivity.class);
            startActivity(login);
        }

        mbase = FirebaseDatabase.getInstance().getReference("user").child(uid);

        recyclerView = findViewById(R.id.recycler1);

        recyclerView.setLayoutManager(
                new LinearLayoutManager(this));

        // It is a class provide by the FirebaseUI to make a
        // query in the database to fetch appropriate data
        FirebaseRecyclerOptions<histori> options
                = new FirebaseRecyclerOptions.Builder<histori>()
                .setQuery(mbase.child("history"), histori.class)
                .build();
        // Connecting object of required Adapter class to
        // the Adapter class itself
        adapter = new historiAdapter(options);
        // Connecting Adapter class with the Recycler view*/
        recyclerView.setAdapter(adapter);
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
}
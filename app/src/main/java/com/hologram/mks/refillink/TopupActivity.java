package com.hologram.mks.refillink;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TopupActivity extends AppCompatActivity {

    DatabaseReference userRef;

    FirebaseAuth auth;
    FirebaseUser user;

    String uid;
    long saldo;

    TextView saldoTxt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        userRef = database.getReference("user");

        saldoTxt = findViewById(R.id.saldo3Txt);
        auth = FirebaseAuth.getInstance();
        user = auth.getCurrentUser();
        if (user != null) {

            uid = user.getUid();

            // User is signed in
//            Toast.makeText(this, "ADA " + name, Toast.LENGTH_SHORT).show();
        } else {
            // No user is signed in
            Toast.makeText(this, "Silahkan Login", Toast.LENGTH_SHORT).show();
            Intent login = new Intent(TopupActivity.this, LoginActivity.class);
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
    }
}
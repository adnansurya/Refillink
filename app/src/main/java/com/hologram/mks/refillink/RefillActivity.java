package com.hologram.mks.refillink;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("ALL")
public class RefillActivity extends AppCompatActivity {

    Button refillBtn1, refillBtn2, refillBtn3;
    TextView kapasitasTxt, persenTxt, titleTxt, instruksiTxt, saldoTxt;

    String kapasitasStr, persenStr, kodeMesin;

    DatabaseReference historyRef, mesinRef, userRef;

    Double tersediaLg, kapasitasLg;

    FirebaseAuth auth;
    FirebaseUser user;
    String name, email;
    String uid = null;

    boolean pressed = false;
    long saldo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill);

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
            Intent login = new Intent(RefillActivity.this, LoginActivity.class);
            startActivity(login);
        }




        kodeMesin = getIntent().getExtras().getString("kode");


        refillBtn1 = findViewById(R.id.refillBtn1);
        refillBtn2 = findViewById(R.id.refillBtn2);
        refillBtn3 = findViewById(R.id.refillBtn3);

        kapasitasTxt = findViewById(R.id.kapasitasTxt);
        persenTxt = findViewById(R.id.persenTxt);
        titleTxt = findViewById(R.id.titleTxt);
        instruksiTxt = findViewById(R.id.instruksiTxt);
        saldoTxt = findViewById(R.id.saldo2Txt);

        titleTxt.setText(kodeMesin);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        mesinRef = database.getReference("mesin").child(kodeMesin);
        historyRef = database.getReference("history");
        userRef = database.getReference("user");

        if(uid != null){
            userRef.child(uid).addValueEventListener(new ValueEventListener() {
                @SuppressLint("DefaultLocale")
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    saldo = dataSnapshot.child("saldo").getValue(Long.class);
                    saldoTxt.setText("Rp. " + String.valueOf(saldo));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    // Failed to read value
                    Log.w(TAG, "Failed to read value.", error.toException());
                }
            });
        }

        // Read from the database
        mesinRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String dispenserStatus = dataSnapshot.child("relay").getValue(String.class);
                if(dispenserStatus == "OFF"){
                    instruksiTxt.setText("Mengisi...");
                }else  if(dispenserStatus != "OFF"){
                    instruksiTxt.setText("Silakan pilih jumlah air yang diinginkan");
                }
                kapasitasLg = dataSnapshot.child("kapasitas").getValue(Double.class);
                tersediaLg = dataSnapshot.child("tersedia").getValue(Double.class);

                double persenDbl = (tersediaLg *100 /  kapasitasLg);
                Log.e(TAG, "Value is: " + kapasitasLg +" | " + tersediaLg);

                kapasitasTxt.setText(String.format("%.2f L", tersediaLg));
                persenTxt.setText(String.format("%.2f %%", persenDbl));

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        refillBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((saldo -5000 ) >= 0) {
                    mesinRef.child("relay").setValue("750");

                    userRef.child(uid).child("saldo").setValue(saldo - 5000);
                    mesinRef.child("tersedia").setValue(tersediaLg - 0.75);
                    addHistory(kodeMesin, 5000, 0.75);
                    pressed = true;
                }else{
                    instruksiTxt.setText("Silahkan isi saldo");
                    Toast.makeText(RefillActivity.this, "Saldo tidak cukup", Toast.LENGTH_SHORT).show();
                }

            }
        });

        refillBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((saldo -3000 ) >= 0){
                    mesinRef.child("relay").setValue("500");

                    userRef.child(uid).child("saldo").setValue(saldo-3000);
                    mesinRef.child("tersedia").setValue(tersediaLg-0.5);
                    addHistory(kodeMesin, 3000, 0.5);
                    pressed = true;
                }else{
                    instruksiTxt.setText("Silahkan isi saldo");
                    Toast.makeText(RefillActivity.this, "Saldo tidak cukup", Toast.LENGTH_SHORT).show();
                }

            }
        });
        refillBtn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((saldo -1500 ) >= 0) {
                    mesinRef.child("relay").setValue("250");
                    userRef.child(uid).child("saldo").setValue(saldo - 1500);
                    mesinRef.child("tersedia").setValue(tersediaLg - 0.25);
                    addHistory(kodeMesin, 1500, 0.25);
                    pressed = true;
                }else{
                    instruksiTxt.setText("Silahkan isi saldo");
                    Toast.makeText(RefillActivity.this, "Saldo tidak cukup", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }



    void addHistory(String kode, long harga, double jumlah){
        @SuppressLint("SimpleDateFormat") String waktu= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        String key = historyRef.push().getKey();

        Map<String, Object> historyObj = new HashMap<String, Object>();
        historyObj.put("kode_mesin", kode);
        historyObj.put("harga", harga);
        historyObj.put("jumlah", jumlah);
        historyObj.put("waktu", waktu);
        historyObj.put("user", uid);

        historyRef.child(key).updateChildren(historyObj);



        historyObj.remove("user");


        userRef.child(uid).child("history").child(key).setValue(historyObj);
    }
}
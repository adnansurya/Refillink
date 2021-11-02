package com.hologram.mks.refillink;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

@SuppressWarnings("ALL")
public class RefillActivity extends AppCompatActivity {

    Button refillBtn1, refillBtn2, refillBtn3;
    TextView kapasitasTxt, persenTxt;

    String kapasitasStr, persenStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refill);

        refillBtn1 = findViewById(R.id.refillBtn1);
        refillBtn2 = findViewById(R.id.refillBtn2);
        refillBtn3 = findViewById(R.id.refillBtn3);

        kapasitasTxt = findViewById(R.id.kapasitasTxt);
        persenTxt = findViewById(R.id.persenTxt);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("mesin").child("dispenser1");

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("DefaultLocale")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Long kapasitasLg = dataSnapshot.child("kapasitas").getValue(Long.class);
                Long tersediaLg = dataSnapshot.child("tersedia").getValue(Long.class);

                Long persenDbl = (tersediaLg *100 /  kapasitasLg);
                Log.e(TAG, "Value is: " + kapasitasLg +" | " + tersediaLg);



                kapasitasTxt.setText(String.format("%d L", kapasitasLg));
                persenTxt.setText(String.format("%d %%", persenDbl));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

       


    }
}
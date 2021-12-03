package com.hologram.mks.refillink;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.IdpResponse;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String name = user.getDisplayName();
            String email = user.getEmail();
            Intent login = new Intent(this,MainActivity.class);
            startActivity(login);

            // User is signed in
            Toast.makeText(this, "Selamat Datang, " + name, Toast.LENGTH_SHORT).show();
        } else {
            // No user is signed in
            Toast.makeText(this, "Silahkan Login", Toast.LENGTH_SHORT).show();
            List<AuthUI.IdpConfig> providers = Collections.singletonList(
                    new AuthUI.IdpConfig.EmailBuilder().build());
//                new AuthUI.IdpConfig.PhoneBuilder().build(),
//                new AuthUI.IdpConfig.GoogleBuilder().build(),
//                new AuthUI.IdpConfig.FacebookBuilder().build(),
//                new AuthUI.IdpConfig.TwitterBuilder().build());

// Create and launch sign-in intent
            Intent signInIntent = AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setTheme(R.style.LoginTheme)
                    .setAvailableProviders(providers)
//                    .setIsSmartLockEnabled(false)
                    .build();
            signInLauncher.launch(signInIntent);

        }
//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);

        // Choose authentication providers

    }

    private void onSignInResult(FirebaseAuthUIAuthenticationResult result) {
        IdpResponse response = result.getIdpResponse();
        if (result.getResultCode() == RESULT_OK) {
            // Successfully signed in
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if(user != null){
                Date currentTime = Calendar.getInstance().getTime();
                @SuppressLint("SimpleDateFormat") String waktu= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                String fullName = user.getDisplayName();
                String userId = user.getUid();
                String userEmail = user.getEmail();

                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("user").child(userId);



                myRef.get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.e("firebase error:", "Error getting data", task.getException());
                    }else {
                        if(!task.getResult().exists()){
                            String loginData = String.valueOf(task.getResult().getValue());

                            Map<String, Object> userObj = new HashMap<String, Object>();
                            userObj.put("nama", fullName);
                            userObj.put("email", userEmail);
                            userObj.put("waktu_daftar", waktu);
                            userObj.put("saldo", 0);

                            myRef.setValue(userObj);
                        }


                        Toast.makeText(LoginActivity.this, fullName, Toast.LENGTH_SHORT).show();
                        Intent gotoMain= new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(gotoMain);
                        Log.d("firebase data:", String.valueOf(task.getResult()));
                    }
                });
            }else{
                Toast.makeText(LoginActivity.this, "User tidak ditemukan", Toast.LENGTH_SHORT).show();
            }


            // ...
        } else {
            // Sign in failed. If response is null the user canceled the
            // sign-in flow using the back button. Otherwise check
            if (response != null) {
                final int errorCode = response.getError().getErrorCode();
                Toast.makeText(LoginActivity.this, "GAGAL : " + errorCode, Toast.LENGTH_SHORT).show();
            }

            // ...
        }
    }

    // See: https://developer.android.com/training/basics/intents/result
    private final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    onSignInResult(result);
                }
            }
    );


    @Override
    public void onBackPressed() {
        finish();
    }
}
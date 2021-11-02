package com.hologram.mks.refillink;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class DaftarActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    EditText fullNameTxt, emailTxt, passwordTxt, password2Txt;
    Button daftarBtn;
    TextView explainTxt;

    String fullName, email, password, password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar);

        fullNameTxt = findViewById(R.id.fulllNameTxt);
        emailTxt = findViewById(R.id.emailTxt);
        passwordTxt = findViewById(R.id.passwordTxt);
        password2Txt = findViewById(R.id.password2Txt);
        daftarBtn = findViewById(R.id.daftarBtn);
        explainTxt = findViewById(R.id.explainTxt);


        daftarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fullName = fullNameTxt.getText().toString();
                email = emailTxt.getText().toString();
                password = passwordTxt.getText().toString();
                password2 = password2Txt.getText().toString();
                if(password.equals(password2)){


                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(DaftarActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "createUserWithEmail:success");
                                        FirebaseUser user = mAuth.getCurrentUser();
//                                        assert user != null;
//                                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
//                                                .setDisplayName(fullName).build();
//
//                                        user.updateProfile(profileUpdates)
//                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
//                                                    @Override
//                                                    public void onComplete(@NonNull Task<Void> task) {
//                                                        if (task.isSuccessful()) {
//                                                            Log.d(TAG, "User profile updated.");
//                                                        }
//                                                    }
//                                                });

                                        Toast.makeText(DaftarActivity.this, "User : berhasil didaftarkan." , Toast.LENGTH_SHORT).show();

                                    } else {
                                        // If sign in fails, display a message to the user.
                                        Log.d(TAG, "createUserWithEmail:failure", task.getException());
                                        Toast.makeText(DaftarActivity.this, "Hai", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                }else{
                    explainTxt.setText(R.string.password_unmatch);
                }
            }
        });
    }
}
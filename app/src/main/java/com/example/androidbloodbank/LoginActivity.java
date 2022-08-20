package com.example.androidbloodbank;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private TextView backButton;

    private TextInputEditText loginEmail, loginPassword;

    private TextView forgotPassword;

    private Button loginButton;

    private ProgressDialog loader;

    private FirebaseAuth mAuth;

    private DatabaseReference userDatabaseRef;

    private FirebaseAuth.AuthStateListener authStateListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String currentUserId = mAuth.getCurrentUser().getUid();
                    userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("is_admin");
                    userDatabaseRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.exists()) {
                                Intent intent;
                                int is_admin = snapshot.getValue(Integer.class);
                                if (is_admin == 1) {
                                    intent = new Intent(LoginActivity.this, AdminActivity.class);
                                    startActivity(intent);
                                    finish();


                                } else if(is_admin == 0){
                                    intent = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();

                                }
//                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
//                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//                    startActivity(intent);
                }
            }
        };

        backButton = findViewById(R.id.backButton);
        loginEmail = findViewById(R.id.loginEmail);
        loginPassword = findViewById(R.id.loginPassword);
        loginButton = findViewById(R.id.loginButton);
        forgotPassword = findViewById(R.id.forgotPassword);

        loader = new ProgressDialog(this);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText resetMail = new EditText(view.getContext());
                AlertDialog.Builder passwordResetDilog = new AlertDialog.Builder(view.getContext());
                passwordResetDilog.setTitle("Reset Password?");
                passwordResetDilog.setMessage("Enter Your Email To Received Reset Link.");
                passwordResetDilog.setView(resetMail);

                passwordResetDilog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        String mail = resetMail.getText().toString();
                        mAuth.sendPasswordResetEmail(mail).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(LoginActivity.this, "Reset Link Sent To Your Email", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(LoginActivity.this, "Error ! Reset Link is Not Sent" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                passwordResetDilog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });

                passwordResetDilog.create().show();
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SelectRegistrationActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = loginEmail.getText().toString().trim();
                final String password = loginPassword.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    loginEmail.setError("Email is required!");
                }
                if (TextUtils.isEmpty(password)) {
                    loginPassword.setError("Password is required!");
                } else {

                    loader.setMessage("Log in progress");
                    loader.setCanceledOnTouchOutside(false);
                    loader.show();

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task){
                            if (task.isSuccessful()) {
//                                if(FirebaseAuth.getInstance().getCurrentUser().isEmailVerified()){
                                   Toast.makeText(LoginActivity.this, "Log in successful", Toast.LENGTH_SHORT).show();
//
//                                }
//                                else{
//                                    Toast.makeText(LoginActivity.this, "Please verify your email Id.", Toast.LENGTH_SHORT).show();
//
//                                }


                                //String currentUserId = mAuth.getCurrentUser().getUid();
                                //                                userDatabaseRef = FirebaseDatabase.getInstance().getReference().child("users").child(currentUserId).child("is_admin");
                                //                                userDatabaseRef.addValueEventListener(new ValueEventListener() {
                                //                                    @Override
                                //                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                //                                        if (snapshot.exists()) {
                                //                                            int is_admin = snapshot.getValue(Integer.class);
                                //                                             Log.e("fdfdf", String.valueOf(is_admin));
                                //                                            if (is_admin == 1) {
                                //                                                Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
                                //                                                startActivity(intent);
                                //
                                //                                            } else if (is_admin == 0) {
                                //                                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                //                                                startActivity(intent);
                                //                                            }
                                //                                        }
                                //                                    }
                                //
                                //                                    @Override
                                //                                    public void onCancelled(@NonNull DatabaseError error) {
                                //
                                //                                    }
                                //                                });
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                            }
                            loader.dismiss();
                        }
                    });
                }
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);

    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}
package com.example.quizztemple;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.quizztemple.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore database;
    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();
        dialog = new ProgressDialog(this);
        dialog.setMessage("we are creating new account");
        dialog.setCancelable(false);
        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, pass, name, referCode;

                email = binding.emailBox.getText().toString();
                pass = binding.passwordBox.getText().toString();
                name = binding.nameBox.getText().toString();
                referCode = binding.referBox.getText().toString();

                if (email.isEmpty() && pass.isEmpty() && name.isEmpty()) {
                    binding.emailBox.setError("Required");
                     binding.passwordBox.setError("Required");
                    binding.nameBox.setError("Required");
                    //Toast.makeText(SignupActivity.this, "Enter signup details", Toast.LENGTH_SHORT).show();
                    return;
                }
                else{
                User user = new User(name, email, pass, referCode);
                dialog.show();
                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                                auth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task1) {
                                        if(task1.isSuccessful()){
                                            Toast.makeText(SignupActivity.this, "please verify email", Toast.LENGTH_SHORT).show();
                                            String uid = task.getResult().getUser().getUid();
                                            database
                                                    .collection("users")
                                                    .document(uid)
                                                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        dialog.dismiss();
                                                        startActivity(new Intent(SignupActivity.this, LoginActivity.class));
                                                        finish();
                                                    } else {
                                                        Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });
                                        }
                                        else
                                        {
                                            Toast.makeText(SignupActivity.this, task1.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                                        }


                                    }
                                });

                        } else {
                            dialog.dismiss();
                            Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            }
        });
        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignupActivity.this,LoginActivity.class));
            }
        });
    }
};
package com.bluesoftit.cashcoin.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bluesoftit.cashcoin.Models.User;
import com.bluesoftit.cashcoin.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth;
    FirebaseFirestore database;
    ProgressDialog dialog;
    String email, pass, name, referCode, myRefer, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        auth = FirebaseAuth.getInstance();
        database = FirebaseFirestore.getInstance();

        dialog = new ProgressDialog(this);
        dialog.setMessage("We're creating new account...");

        binding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            }
        });


        binding.createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
                dialog.setCanceledOnTouchOutside(false);
                createAccount();

            }
        });


    }

    public void createAccount() {
        email = binding.emailBox.getText().toString();
        pass = binding.passwordBox.getText().toString();
        name = binding.nameBox.getText().toString();
        referCode = binding.referBox.getText().toString();

        if (email.isEmpty() || pass.isEmpty() || name.isEmpty() || referCode.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Please fill all the information correctly.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else {
            Query query = database.collection("users").whereEqualTo("myRefer", referCode);
            query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            String UserId = documentSnapshot.getString("userId");
                            if (documentSnapshot.exists()) {
                                auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            String uid = task.getResult().getUser().getUid();
                                            String tempRefer = String.valueOf(uid.getBytes());
                                            myRefer = tempRefer.substring(2);
                                            userId = uid;
                                            final User user = new User(name, email, pass, referCode, myRefer, userId);
                                            database
                                                    .collection("users")
                                                    .document(uid)
                                                    .set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                dialog.dismiss();
                                                                startActivity(new Intent(SignupActivity.this, MainActivity.class));
                                                                finish();
                                                            } else {
                                                                Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                                            }
                                                        }
                                                    });
                                            database
                                                    .collection("users")
                                                    .document(uid)
                                                    .update("coins", FieldValue.increment(+75)).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(SignupActivity.this, "Successfully you gain your commission.", Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                            if (UserId != null) {
                                                database
                                                        .collection("users")
                                                        .document(UserId)
                                                        .update("coins", FieldValue.increment(+50));
                                            }
                                        } else {
                                            dialog.dismiss();
                                            Toast.makeText(SignupActivity.this, task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            } else {
                                Toast.makeText(SignupActivity.this, "Refer Code is wrong", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }
    }


}
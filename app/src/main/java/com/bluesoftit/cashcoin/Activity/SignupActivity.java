package com.bluesoftit.cashcoin.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bluesoftit.cashcoin.Models.User;
import com.bluesoftit.cashcoin.databinding.ActivitySignupBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    ActivitySignupBinding binding;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    ProgressDialog dialog;
    String email, pass, name, referedBy, referCode, userId;
    String url = "http://admin.digitalstore.bluesoftit.xyz/myApi/register.php";
    FirebaseFirestore database = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

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
                dialog.setCanceledOnTouchOutside(false);
                dialog.setTitle("Creating");
                dialog.setMessage("We are creating your account");
                dialog.show();
                createAccount();

            }
        });


    }

    public void createAccount() {
        email = binding.emailBox.getText().toString();
        pass = binding.passwordBox.getText().toString();
        name = binding.nameBox.getText().toString();
        referedBy = binding.referBox.getText().toString();

        if (email.isEmpty() || pass.isEmpty() || name.isEmpty() || referedBy.isEmpty()) {
            Toast.makeText(SignupActivity.this, "Please fill all the information correctly.", Toast.LENGTH_SHORT).show();
            dialog.dismiss();
        } else {
            Query query = database.collection("users").whereEqualTo("referCode", referedBy);
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
                                            referCode = tempRefer.substring(2);
                                            userId = uid;
                                            final User user = new User(name, email, pass, referedBy, referCode, userId);
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
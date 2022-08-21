package com.bluesoftit.cashcoin.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bluesoftit.cashcoin.Models.User;
import com.bluesoftit.cashcoin.databinding.ProductDetailsBinding;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startapp.sdk.adsbase.StartAppAd;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;


public class ProductDetails extends AppCompatActivity {

    ProductDetailsBinding binding;
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    String productName;
    ProgressDialog dialog;
    User user;
    StartAppAd startAppAd;
    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setProductInfo();

        binding.confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String info = binding.info.getText().toString();
                String phone = binding.phone.getText().toString();
               if (info.isEmpty()||phone.isEmpty()){
                   Toast.makeText(ProductDetails.this, "Fill all the information.", Toast.LENGTH_SHORT).show();
               }else {
                   pay();
               }
            }
        });









    }

    public void setProductInfo(){
        final String productId = getIntent().getStringExtra("productId");
        productName = getIntent().getStringExtra("productName");
        final String productImg = getIntent().getStringExtra("productImg");
        final String productBrif = getIntent().getStringExtra("productBrif");
        final String productPrice = getIntent().getStringExtra("productPrice");

        Glide.with(this)
                .load(productImg)
                .into(binding.pImg);
        binding.pName.setText(productName);
        binding.pBrif.setText(productBrif);
        binding.pPrice.setText(String.valueOf(productPrice));

    }

    public void addOrder(){
        String uid = auth.getCurrentUser().getUid();
        productName = getIntent().getStringExtra("productName");
        dialog = ProgressDialog.show(ProductDetails.this, "Processing",
                "Your order is processing. Please wait...", true);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://admin.digitalstore.bluesoftit.xyz:80/api/orders/add?x-Api-Key=44400859C853285124E604A6B54EBBAA";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Display the first 500 characters of the response string.
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")){
                                Toast.makeText(ProductDetails.this, "Your order is under review.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ProductDetails.this,DigitalStore.class);
                                startActivity(intent);
                                dialog.dismiss();
                            }else {
                                Toast.makeText(ProductDetails.this, "Your Data is not fetched.", Toast.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            dialog.dismiss();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error!=null)
                    dialog.dismiss();
                Toast.makeText(ProductDetails.this, "Unable to process order. Please check your connection and try again", Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("product",productName);
                params.put("orderInfo",binding.info.getText().toString());
                params.put("phone",String.valueOf(binding.phone.getText().toString()));
                params.put("uid",uid);
                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("x-Api-Key","44400859C853285124E604A6B54EBBAA");
                return headers;
            }
        };
        queue.add(stringRequest);
    }

    private void pay(){
        final String productPrice = getIntent().getStringExtra("productPrice");
        database.collection("users")
                .document(auth.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);

                        if (user.getCoins()>=Long.parseLong(productPrice)){
                            database.collection("users")
                                     .document(auth.getUid())
                                     .update("coins",FieldValue.increment(-Long.parseLong(productPrice))).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(ProductDetails.this, "Your payment is successful", Toast.LENGTH_SHORT).show();
                                            addOrder();
                                            if (startAppAd.isReady()){
                                                StartAppAd.showAd(ProductDetails.this);
                                            }
                                        }
                                    });
                        }else {
                            Toast.makeText(ProductDetails.this, "You don't have enough CC.", Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }


    public void updateCoins(){
        String uid = FirebaseAuth.getInstance().getUid();
        final String productPrice = getIntent().getStringExtra("productPrice");
        if (user.getCoins() > 500){
            database.collection("users")
                    .document(uid)
                    .update("coins", FieldValue.increment(-500)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(ProductDetails.this, "You paid product price successfully!", Toast.LENGTH_SHORT).show();
                          //  addOrder();
                        }
                    });

        }else {
            Toast.makeText(this, "You don't have enough CC", Toast.LENGTH_SHORT).show();
        }
    }
}
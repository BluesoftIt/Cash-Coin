package com.bluesoftit.cashcoin.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bluesoftit.cashcoin.Adapters.productViewAdapter;
import com.bluesoftit.cashcoin.Models.User;
import com.bluesoftit.cashcoin.Models.productViewModel;
import com.bluesoftit.cashcoin.R;
import com.bluesoftit.cashcoin.databinding.DigitalStoreBinding;
import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DigitalStore extends AppCompatActivity {

    DigitalStoreBinding binding;
    private ImageSlider shopSlider;
    FirebaseFirestore database;
    User user;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    ArrayList<productViewModel>productViewModels;
    productViewAdapter productViewAdapter;
    ProgressDialog dialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DigitalStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        shopSlider = findViewById(R.id.shop_slider);
        database = FirebaseFirestore.getInstance();


        getCoins();
        setSlider();
        setCategories();
        setRecentProducts();


    }

    public void setSlider(){
        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/play-cash-9f26c.appspot.com/o/digitalStore%2Ffb_boost_banner.jpg?alt=media&token=b1fc3342-10ab-422a-9e8f-1b97203b94eb", "Facebook Post Boost & increase your audience",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/play-cash-9f26c.appspot.com/o/digitalStore%2Ffb_follow_banner.png?alt=media&token=31026a9c-cc94-4e22-89a6-2346d164e0a6", "Increase your followers, Purchase followers with Cash Coin", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/play-cash-9f26c.appspot.com/o/digitalStore%2Fff_banner.jpg?alt=media&token=6b53dd54-cada-40a2-9de8-2eacbdee17f2", "Purchase Freefire diamond & Membership with Cash Coin", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/play-cash-9f26c.appspot.com/o/digitalStore%2Fgoogle_gift_banner.png?alt=media&token=7b9b23bb-5ffd-433b-847e-09b27f6803f9", "Purchase Google play gift cards with Cash Coin", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/play-cash-9f26c.appspot.com/o/digitalStore%2Fpubg_bannr.png?alt=media&token=f77cdd29-ee57-4545-b510-8b2a225a6e8a", "Purchase PUBG UC & Royal pass with Cash Coin", ScaleTypes.FIT));
        shopSlider.setImageList(slideModels,ScaleTypes.FIT);
    }

    public void setCategories(){

        String ff_topup = "http://admin.digitalstore.bluesoftit.xyz/uploads/categories/20220719161655-2022-07-19categories161654.png";
        String pubg_topup = "http://admin.digitalstore.bluesoftit.xyz/uploads/categories/20220719161701-2022-07-19categories161659.png";
        String fb_boost = "http://admin.digitalstore.bluesoftit.xyz/uploads/categories/20220719161712-2022-07-19categories161711.jpg";
        String fb_like = "http://admin.digitalstore.bluesoftit.xyz/uploads/categories/20220719161755-2022-07-19categories161753.jpg";
        String visa_master = "http://admin.digitalstore.bluesoftit.xyz/uploads/categories/20220719161721-2022-07-19categories161719.png";
        String gift_cards = "http://admin.digitalstore.bluesoftit.xyz/uploads/categories/20220719161745-2022-07-19categories161742.png";
        String youtube_boost = "http://admin.digitalstore.bluesoftit.xyz/uploads/categories/20220804125509-2022-08-04categories125506.jpg";


        Glide.with(this)
                .load(ff_topup)
                .into(binding.ffTopup);
        Glide.with(this)
                .load(pubg_topup)
                .into(binding.pubgTopup);
        Glide.with(this)
                .load(fb_boost)
                .into(binding.fbBoost);
        Glide.with(this)
                .load(fb_like)
                .into(binding.fbLike);
        Glide.with(this)
                .load(visa_master)
                .into(binding.visaMaster);
        Glide.with(this)
                .load(gift_cards)
                .into(binding.giftCards);
        Glide.with(this)
                .load(youtube_boost)
                .into(binding.youtube);

        binding.youtube.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = "Youtube";
                Intent intent = new Intent(DigitalStore.this,CategoriesProducts.class);
                intent.putExtra("catName",catName);
                intent.putExtra("catImg",youtube_boost);
                startActivity(intent);
            }
        });

        binding.ffTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = "Freefire";
                Intent intent = new Intent(DigitalStore.this,CategoriesProducts.class);
                intent.putExtra("catName",catName);
                intent.putExtra("catImg",ff_topup);
                startActivity(intent);
            }
        });

        binding.fbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = "FB Like & Follow";
                Intent intent = new Intent(DigitalStore.this,CategoriesProducts.class);
                intent.putExtra("catName",catName);
                intent.putExtra("catImg",fb_like);
                startActivity(intent);
            }
        });

        binding.fbBoost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = "FB Boost";
                Intent intent = new Intent(DigitalStore.this,CategoriesProducts.class);
                intent.putExtra("catName",catName);
                intent.putExtra("catImg",fb_boost);
                startActivity(intent);
            }
        });

        binding.pubgTopup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = "PUBG";
                Intent intent = new Intent(DigitalStore.this,CategoriesProducts.class);
                intent.putExtra("catName",catName);
                intent.putExtra("catImg",pubg_topup);
                startActivity(intent);
            }
        });

        binding.giftCards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = "Gift Cards";
                Intent intent = new Intent(DigitalStore.this,CategoriesProducts.class);
                intent.putExtra("catName",catName);
                intent.putExtra("catImg",gift_cards);
                startActivity(intent);
            }
        });

        binding.visaMaster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String catName = "Visa / Master Cards";
                Intent intent = new Intent(DigitalStore.this,CategoriesProducts.class);
                intent.putExtra("catName",catName);
                intent.putExtra("catImg",gift_cards);
                startActivity(intent);
            }
        });



    }

    public void getCoins(){
        String uid = auth.getUid().toString();
        database.collection("users")
                .document(uid)
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        binding.availableCoins.setText(String.valueOf(user.getCoins())+" CC");

                    }
                });
    }

    public void setRecentProducts(){
        productViewModels = new ArrayList<>();
        binding.dsProductList.setLayoutManager(new GridLayoutManager(this,2));
        productViewAdapter = new productViewAdapter(this,productViewModels);
        binding.dsProductList.setAdapter(productViewAdapter);
        dialog = ProgressDialog.show(DigitalStore.this, "Updating Product",
                "Please wait...", true);
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://admin.digitalstore.bluesoftit.xyz:80/api/products/all?X-Api-Key=44400859C853285124E604A6B54EBBAA&filter=&field=&start=&limit=8&sort_field=&sort_order=ASC";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                       try {
                            JSONObject jsonObject = new JSONObject(response);
                            if (jsonObject.getBoolean("status")){
                                JSONArray productsArray = jsonObject.getJSONObject("data").getJSONArray("products");
                                for (int i = 0; i<productsArray.length();i++){
                                    JSONObject productObj = productsArray.getJSONObject(i);
                                    productViewModel productViewModel = new productViewModel(
                                            productObj.getString("pName"),
                                            productObj.getString("pBrif"),
                                            productObj.getString("pImg"),
                                            productObj.getInt("pPrice"),
                                            productObj.getInt("id")
                                    );
                                    productViewModels.add(productViewModel);
                                    dialog.dismiss();
                                }
                                productViewAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(DigitalStore.this, "Your Data is not fatched.", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(DigitalStore.this, "something went wrong please check your connection.", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        queue.add(stringRequest);
    }
}
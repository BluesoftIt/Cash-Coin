package com.bluesoftit.cashcoin.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bluesoftit.cashcoin.Adapters.productViewAdapter;
import com.bluesoftit.cashcoin.Models.productViewModel;
import com.bluesoftit.cashcoin.databinding.ActivityCategoriesProductsBinding;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CategoriesProducts extends AppCompatActivity {

    ArrayList<productViewModel>productViewModels;
    productViewAdapter productViewAdapter;
    ActivityCategoriesProductsBinding binding;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCategoriesProductsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String catTitle = getIntent().getStringExtra("catName");
        String catIcon = getIntent().getStringExtra("catImg");

        String ffProducts = "Freefire";
        String PubgProducts = "PUBG";
        String VisaMasterProducts = "Visa / Master Cards";
        String FbBoostProducts = "FB Boost";
        String FbLikeProducts = "FB Like & Follow";
        String GiftCardProducts = "Gift Cards";
        final String YoutubeProducts = "Youtube";

     Glide.with(this)
                .load(catIcon)
                .into(binding.catIco);
       binding.catTitle.setText(catTitle);
       if(catTitle.equals(ffProducts)){
           getFFProducts();
           binding.ffProduct.setVisibility(View.VISIBLE);
       }else if(catTitle.equals(PubgProducts)) {
           getPubgProducts();
           binding.pubgProduct.setVisibility(View.VISIBLE);
       }else if (catTitle.equals(VisaMasterProducts)){
           getVisaProducts();
           getMasterProducts();
           binding.visaProduct.setVisibility(View.VISIBLE);
           binding.masterProduct.setVisibility(View.VISIBLE);
       }else if(catTitle.equals(FbBoostProducts)){
           getFbBoostProducts();
           binding.fbBoostProduct.setVisibility(View.VISIBLE);
       }else if(catTitle.equals(FbLikeProducts)){
            getFbLikeProducts();
            binding.fbLikeProduct.setVisibility(View.VISIBLE);
       }else if(catTitle.equals(GiftCardProducts)){
            getGiftCardsProducts();
            binding.giftCardsProduct.setVisibility(View.VISIBLE);
       }else if (catTitle.equals(YoutubeProducts)){
           getYoutubeProducts();
           binding.youtubeProduct.setVisibility(View.VISIBLE);
       }




    }


    public void getFFProducts(){
        productViewModels = new ArrayList<>();
        binding.ffProduct.setLayoutManager(new GridLayoutManager(this,3));
        productViewAdapter = new productViewAdapter(this,productViewModels);
        binding.ffProduct.setAdapter(productViewAdapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://admin.digitalstore.bluesoftit.xyz:80/api/products/all?X-Api-Key=44400859C853285124E604A6B54EBBAA&filter=freefire&field=&start=&limit=&sort_field=&sort_order=ASC";
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
                                }
                                productViewAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(CategoriesProducts.this, "Your Data is fatched.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoriesProducts.this, "Error: Something went wrong. Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void getPubgProducts(){
        productViewModels = new ArrayList<>();
        binding.pubgProduct.setLayoutManager(new GridLayoutManager(this,3));
        productViewAdapter = new productViewAdapter(this,productViewModels);
        binding.pubgProduct.setAdapter(productViewAdapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://admin.digitalstore.bluesoftit.xyz:80/api/products/all?X-Api-Key=44400859C853285124E604A6B54EBBAA&filter=pubg&field=&start=&limit=&sort_field=&sort_order=ASC";
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
                                }
                                productViewAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(CategoriesProducts.this, "Your Data is fatched.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoriesProducts.this, "Error: Something went wrong. Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void getFbBoostProducts(){
        productViewModels = new ArrayList<>();
        binding.fbBoostProduct.setLayoutManager(new GridLayoutManager(this,2));
        productViewAdapter = new productViewAdapter(this,productViewModels);
        binding.pubgProduct.setAdapter(productViewAdapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://admin.digitalstore.bluesoftit.xyz:80/api/products/all?X-Api-Key=44400859C853285124E604A6B54EBBAA&filter=fb+boost&field=&start=&limit=&sort_field=&sort_order=ASC";
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
                                }
                                productViewAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(CategoriesProducts.this, "Your Data is fatched.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoriesProducts.this, "Error: Something went wrong. Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void getFbLikeProducts(){
        productViewModels = new ArrayList<>();
        binding.fbLikeProduct.setLayoutManager(new GridLayoutManager(this,3));
        productViewAdapter = new productViewAdapter(this,productViewModels);
        binding.fbLikeProduct.setAdapter(productViewAdapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://admin.digitalstore.bluesoftit.xyz:80/api/products/all?X-Api-Key=44400859C853285124E604A6B54EBBAA&filter=fb+like&field=&start=&limit=&sort_field=&sort_order=ASC";
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
                                }
                                productViewAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(CategoriesProducts.this, "Your Data is fatched.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoriesProducts.this, "Error: Something went wrong. Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void getGiftCardsProducts(){
        productViewModels = new ArrayList<>();
        binding.giftCardsProduct.setLayoutManager(new GridLayoutManager(this,3));
        productViewAdapter = new productViewAdapter(this,productViewModels);
        binding.giftCardsProduct.setAdapter(productViewAdapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://admin.digitalstore.bluesoftit.xyz:80/api/products/all?X-Api-Key=44400859C853285124E604A6B54EBBAA&filter=gift+card&field=&start=&limit=&sort_field=&sort_order=ASC";
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
                                }
                                productViewAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(CategoriesProducts.this, "Your Data is fatched.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoriesProducts.this, "Error: Something went wrong. Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void getVisaProducts(){
        productViewModels = new ArrayList<>();
        binding.visaProduct.setLayoutManager(new GridLayoutManager(this,3));
        productViewAdapter = new productViewAdapter(this,productViewModels);
        binding.visaProduct.setAdapter(productViewAdapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://admin.digitalstore.bluesoftit.xyz:80/api/products/all?X-Api-Key=44400859C853285124E604A6B54EBBAA&filter=visa&field=&start=&limit=&sort_field=&sort_order=ASC";
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
                                }
                                productViewAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(CategoriesProducts.this, "Your Data is fatched.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoriesProducts.this, "Error: Something went wrong. Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void getMasterProducts(){
        productViewModels = new ArrayList<>();
        binding.masterProduct.setLayoutManager(new GridLayoutManager(this,3));
        productViewAdapter = new productViewAdapter(this,productViewModels);
        binding.masterProduct.setAdapter(productViewAdapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://admin.digitalstore.bluesoftit.xyz:80/api/products/all?X-Api-Key=44400859C853285124E604A6B54EBBAA&filter=master&field=&start=&limit=&sort_field=&sort_order=ASC";
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
                                }
                                productViewAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(CategoriesProducts.this, "Your Data is fatched.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoriesProducts.this, "Error: Something went wrong. Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void getYoutubeProducts(){
        productViewModels = new ArrayList<>();
        binding.youtubeProduct.setLayoutManager(new GridLayoutManager(this,3));
        productViewAdapter = new productViewAdapter(this,productViewModels);
        binding.youtubeProduct.setAdapter(productViewAdapter);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://admin.digitalstore.bluesoftit.xyz:80/api/products/all?X-Api-Key=44400859C853285124E604A6B54EBBAA&filter=youtube&field=&start=&limit=&sort_field=&sort_order=ASC";
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
                                }
                                productViewAdapter.notifyDataSetChanged();

                            }else {
                                Toast.makeText(CategoriesProducts.this, "Your Data is fatched.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(CategoriesProducts.this, "Error: Something went wrong. Please check your internet connection and try again", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}
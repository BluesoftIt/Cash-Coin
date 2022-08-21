package com.bluesoftit.cashcoin.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bluesoftit.cashcoin.Activity.MainActivity;
import com.bluesoftit.cashcoin.Activity.SignupActivity;
import com.bluesoftit.cashcoin.Models.User;
import com.bluesoftit.cashcoin.R;
import com.bluesoftit.cashcoin.Models.WithdrawRequest;
import com.bluesoftit.cashcoin.databinding.FragmentWalletBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;

import java.util.HashMap;
import java.util.Map;


public class WalletFragment extends Fragment {

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentWalletBinding binding;
    String TAG = "myTag";
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseFirestore database = FirebaseFirestore.getInstance();
    User user;
    StartAppAd startAppAd;
    ProgressDialog dialog;
    InterstitialAd interstitialAd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentWalletBinding.inflate(inflater, container, false);
        getCoins();
        StartAppSDK.init(getContext(),"206986480",false);
        startAppAd = new StartAppAd(getContext());
        startAppAd.loadAd(StartAppAd.AdMode.AUTOMATIC);

        getReferCode();

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.setTitle("Processing");
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                if (binding.withdrawMethode==null){
                    Toast.makeText(getContext(), "Please fill all the information", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                if(user.getCoins() > 50000) {
                    final String uid = FirebaseAuth.getInstance().getUid();
                    String withdrawMethode = binding.withdrawMethode.getText().toString();

                    WithdrawRequest request = new WithdrawRequest(uid,withdrawMethode, user.getName(), user.getCoins());
                    database
                            .collection("withdraws")
                            .document(uid)
                            .set(request).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    database.collection("users")
                                            .document(uid)
                                            .update("coins", FieldValue.increment(-user.getCoins()));
                                    Toast.makeText(getContext(), "Your withdraw request is pending now. You recive your payment in 3 working days.", Toast.LENGTH_SHORT).show();
                                    getCoins();
                                    dialog.dismiss();
                                    if (startAppAd.isReady()){
                                        startAppAd.showAd();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(getContext(), "You need more coins to get withdraw.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        return binding.getRoot();
    }

    private void getReferCode(){
        database.collection("users")
                .document(auth.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        binding.referCode.setText(user.getReferCode());
                    }
                });
    }
    private void getCoins(){
        database.collection("users")
                .document(auth.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        user = documentSnapshot.toObject(User.class);
                        String cc = " CC";
                        binding.currentCoins.setText(String.valueOf(user.getCoins()+cc));


                    }
                });
    }

    private void showInterstitialAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(requireContext(),getString(R.string.admob_interstitial), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd mInterstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        interstitialAd = mInterstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        interstitialAd = null;
                    }
                });
    }


}

package com.bluesoftit.cashcoin.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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


public class WalletFragment extends Fragment {

    public WalletFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    FragmentWalletBinding binding;
    FirebaseFirestore database;
    FirebaseAuth auth;
    User user;
    String TAG = "myTag";
    InterstitialAd admobLocal;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentWalletBinding.inflate(inflater, container, false);
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        showAdmobLocalAd();

        getCoinData();
        String WithdrawMethode = binding.withdrawMethode.getText().toString();

        binding.sendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                  if(user.getCoins() > 5000) {
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
                                getCoinData();
                                if (admobLocal!=null){admobLocal.show(getActivity());}
                            }
                        });

                    } else {
                        Toast.makeText(getContext(), "You need more coins to get withdraw.", Toast.LENGTH_SHORT).show();
                    }


            }
        });




        return binding.getRoot();
    }
    private void getCoinData() {
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
    private void showAdmobLocalAd(){
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(requireContext(),getString(R.string.admob_local), adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd mInterstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        admobLocal = mInterstitialAd;

                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.d(TAG, loadAdError.toString());
                        admobLocal = null;
                    }
                });
    }
}
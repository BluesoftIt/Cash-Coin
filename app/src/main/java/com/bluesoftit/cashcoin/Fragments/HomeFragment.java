package com.bluesoftit.cashcoin.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bluesoftit.cashcoin.Activity.MainActivity;
import com.bluesoftit.cashcoin.Adapters.CategoryAdapter;
import com.bluesoftit.cashcoin.Models.CategoryModel;
import com.bluesoftit.cashcoin.Models.User;
import com.bluesoftit.cashcoin.R;
import com.bluesoftit.cashcoin.Activity.SpinnerActivity;
import com.bluesoftit.cashcoin.databinding.FragmentHomeBinding;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAd;
import com.google.android.gms.ads.rewardedinterstitial.RewardedInterstitialAdLoadCallback;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.startapp.sdk.adsbase.StartAppAd;
import com.startapp.sdk.adsbase.StartAppSDK;
import com.startapp.sdk.adsbase.adlisteners.AdDisplayListener;
import com.startapp.sdk.adsbase.adlisteners.VideoListener;

import java.util.ArrayList;
import java.util.Objects;


public class HomeFragment extends Fragment {


    public HomeFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    FragmentHomeBinding binding;
    FirebaseFirestore database;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    InterstitialAd interstitialAd;
    FirebaseAuth auth;
    StartAppAd startAppAd;
    StartAppAd startAppAd2;
    String TAG = "my tag";
    User users;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        database = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
        databaseReference= firebaseDatabase.getReference("notice");
       // showInterstitialAd();
        StartAppSDK.init(getContext(),"206986480",false);
        startAppAd = new StartAppAd(getContext());
        startAppAd.loadAd(StartAppAd.AdMode.REWARDED_VIDEO);
        startAppAd2 = new StartAppAd(getContext());
        startAppAd2.loadAd(StartAppAd.AdMode.AUTOMATIC);


        final ArrayList<CategoryModel> categories = new ArrayList<>();

        final CategoryAdapter adapter = new CategoryAdapter(getContext(), categories);

        database.collection("categories")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        categories.clear();
                        for (DocumentSnapshot snapshot : value.getDocuments()) {
                            CategoryModel model = snapshot.toObject(CategoryModel.class);
                            model.setCategoryId(snapshot.getId());
                            categories.add(model);
                        }
                        adapter.notifyDataSetChanged();
                    }
                });

            binding.categoryList.setLayoutManager(new GridLayoutManager(getContext(),2));
            binding.categoryList.setAdapter(adapter);




        binding.spinwheel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if (interstitialAd != null) {
                    interstitialAd.show(getActivity());
                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            startActivity(new Intent(getContext(), SpinnerActivity.class));
                        }
                    });
                }*/
                if (startAppAd2.isReady()){
                    startAppAd2.showAd();
                    startActivity(new Intent(getContext(),SpinnerActivity.class));
            }else {
                    Toast.makeText(getContext(), "Your Spin limit is over. Try later", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.showAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startAppAd.isReady()){
                    startAppAd.showAd();
                }
            }
        });
        startAppAd.setVideoListener(new VideoListener() {
            @Override
            public void onVideoCompleted() {
                // Grant user with the reward
                database.collection("users")
                        .document(auth.getUid())
                        .update("coins", FieldValue.increment(+5));
                Toast.makeText(getContext(), "You earned 5 CC", Toast.LENGTH_SHORT).show();
            }
        });

        binding.textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateReferLink();
            }
        });


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String notice = snapshot.getValue(String.class);
                binding.notice.setText("Notice: "+notice);
                binding.notice.setEllipsize(TextUtils.TruncateAt.MARQUEE);
                binding.notice.setSelected(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), ""+error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        // Inflate the layout for this fragment
        return binding.getRoot();
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


    private void generateReferLink(){
        database.collection("users")
                .document(auth.getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        users = documentSnapshot.toObject(User.class);
                        String referCode = users.getReferCode();

                        Intent intent = new Intent(Intent.ACTION_SENDTO);
                        intent.setType("text/plain");
                        String body = "Use my refer code "+referCode+" and download cash coin & get 100 cash coin for free bonus!";
                        String sub = "";
                        intent.putExtra(Intent.EXTRA_TEXT,body);
                        intent.putExtra(Intent.EXTRA_TEXT,sub);
                        startActivity(Intent.createChooser(intent,"Share using"));


                    }
                });

    }
}
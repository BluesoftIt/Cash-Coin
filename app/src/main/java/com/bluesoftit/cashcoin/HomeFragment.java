package com.bluesoftit.cashcoin;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class HomeFragment extends Fragment implements OnUserEarnedRewardListener {


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
    String TAG = "my tag";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);

        database = FirebaseFirestore.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference= firebaseDatabase.getReference("notice");
        showInterstitialAd();

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
                if (interstitialAd != null) {
                    interstitialAd.show(getActivity());
                    interstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdDismissedFullScreenContent() {
                            super.onAdDismissedFullScreenContent();
                            startActivity(new Intent(getContext(), SpinnerActivity.class));
                        }
                    });
                } else {
                    Toast.makeText(getContext(), "Your Spin limit is over. Try later", Toast.LENGTH_SHORT).show();
                }
            }
        });
        binding.showAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadAd();
            }
        });
        binding.textView12.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (interstitialAd!=null){interstitialAd.show(getActivity());}
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                String body = "Hey Iam using Play Cash app for earning extra money. If you want to earn money via playing quiz & watching ads then download using following link: https://play.google.com/store/apps/details?id=com.bluesoftit.playcash";
                String sub = "https://play.google.com/store/apps/details?id=com.bluesoftit.cashcoin";
                share.putExtra(Intent.EXTRA_SUBJECT,sub);
                share.putExtra(Intent.EXTRA_TEXT,body);
                startActivity(Intent.createChooser(share, "Share Using"));
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

    private void loadAd() {
        // Use the test ad unit ID to load an ad.
        RewardedInterstitialAd.load(getContext(), getString(R.string.admob_rewards),
                new AdRequest.Builder().build(),  new RewardedInterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(RewardedInterstitialAd ad) {
                        super.onAdLoaded(ad);
                        ad.show(getActivity(),HomeFragment.this);

                    }
                    @Override
                    public void onAdFailedToLoad(LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Toast.makeText(getContext(), "No ads available right now!", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    @Override
    public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .update("coins", FieldValue.increment(+10));
        Toast.makeText(getContext(), "Congratulation! You earned 10 Play Cash Coin.", Toast.LENGTH_SHORT).show();
    }
}
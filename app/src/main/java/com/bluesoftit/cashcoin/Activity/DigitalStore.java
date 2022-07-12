package com.bluesoftit.cashcoin.Activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import android.os.Bundle;

import com.bluesoftit.cashcoin.Adapters.DsCatAdapter;
import com.bluesoftit.cashcoin.Models.DsCatModel;
import com.bluesoftit.cashcoin.Models.User;
import com.bluesoftit.cashcoin.R;
import com.bluesoftit.cashcoin.databinding.DigitalStoreBinding;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DigitalStore extends AppCompatActivity {

    DigitalStoreBinding binding;
    private ImageSlider shopSlider;
    FirebaseFirestore database;
    User user;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DigitalStoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        shopSlider = findViewById(R.id.shop_slider);
        database = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        getCoinData();

        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/play-cash-9f26c.appspot.com/o/digitalStore%2Ffb_boost_banner.jpg?alt=media&token=b1fc3342-10ab-422a-9e8f-1b97203b94eb", "Facebook Post Boost & increase your audience",ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/play-cash-9f26c.appspot.com/o/digitalStore%2Ffb_follow_banner.png?alt=media&token=31026a9c-cc94-4e22-89a6-2346d164e0a6", "Increase your followers, Purchase followers with Cash Coin", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/play-cash-9f26c.appspot.com/o/digitalStore%2Fff_banner.jpg?alt=media&token=6b53dd54-cada-40a2-9de8-2eacbdee17f2", "Purchase Freefire diamond & Membership with Cash Coin", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/play-cash-9f26c.appspot.com/o/digitalStore%2Fgoogle_gift_banner.png?alt=media&token=7b9b23bb-5ffd-433b-847e-09b27f6803f9", "Purchase Google play gift cards with Cash Coin", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/play-cash-9f26c.appspot.com/o/digitalStore%2Fpubg_bannr.png?alt=media&token=f77cdd29-ee57-4545-b510-8b2a225a6e8a", "Purchase PUBG UC & Royal pass with Cash Coin", ScaleTypes.FIT));
        shopSlider.setImageList(slideModels,ScaleTypes.FIT);

         ArrayList<DsCatModel> dsCatModelArrayList = new ArrayList<>();
         DsCatAdapter dsCatAdapter = new DsCatAdapter(this,dsCatModelArrayList);
        database.collection("ds_categories")
                        .addSnapshotListener(new EventListener<QuerySnapshot>() {
                            @Override
                            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                dsCatModelArrayList.clear();
                                for (DocumentSnapshot snapshot : value.getDocuments()){
                                    DsCatModel dsCatModel = snapshot.toObject(DsCatModel.class);
                                    dsCatModel.setDsCatId(snapshot.getId());
                                    dsCatModelArrayList.add(dsCatModel);
                                }
                                dsCatAdapter.notifyDataSetChanged();
                            }
                        });

        binding.dsCatList.setLayoutManager(new GridLayoutManager(this,4));
        binding.dsCatList.setAdapter(dsCatAdapter);





    }
    private void getCoinData() {
        database.collection("users")
                .document(FirebaseAuth.getInstance().getUid())
                .get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        String cc = " CC";
                        user = documentSnapshot.toObject(User.class);
                        binding.availableCoins.setText(String.valueOf(user.getCoins()+cc));


                    }
                });
    }
}
package com.bluesoftit.cashcoin.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bluesoftit.cashcoin.Activity.ProductDetails;
import com.bluesoftit.cashcoin.Models.productViewModel;
import com.bluesoftit.cashcoin.R;
import com.bluesoftit.cashcoin.databinding.ProductViewBinding;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class productViewAdapter extends RecyclerView.Adapter<productViewAdapter.productViewHolder>{

    Context context;
    ArrayList<productViewModel> productViewModels;

    public productViewAdapter(Context context, ArrayList<productViewModel>productViewModels){
        this.context = context;
        this.productViewModels = productViewModels;
    }

    @NonNull
    @Override
    public productViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.product_view,null);
        return new productViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull productViewHolder holder, int position) {
        productViewModel model = productViewModels.get(position);

        String imgSource = "http://admin.digitalstore.bluesoftit.xyz/uploads/products/"+model.getpImg();
        Glide.with(context)
                .load(imgSource)
                .into(holder.binding.productsImg);

        holder.binding.productsName.setText(model.getpName());
        holder.binding.productBrif.setText(model.getpBrif());
        holder.binding.productsPrice.setText(model.getpPrice() +" CC");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProductDetails.class);
                intent.putExtra("productId",String.valueOf(model.getId()));
                intent.putExtra("productName",model.getpName());
                intent.putExtra("productImg",imgSource);
                intent.putExtra("productBrif",model.getpBrif());
                intent.putExtra("productPrice",String.valueOf(model.getpPrice()));
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return productViewModels.size();
    }

    public class productViewHolder extends RecyclerView.ViewHolder {

        ProductViewBinding binding;
        public productViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProductViewBinding.bind(itemView);

        }
    }
}

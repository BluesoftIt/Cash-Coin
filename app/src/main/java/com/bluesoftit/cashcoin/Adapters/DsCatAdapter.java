package com.bluesoftit.cashcoin.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bluesoftit.cashcoin.Models.DsCatModel;
import com.bluesoftit.cashcoin.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class DsCatAdapter  extends RecyclerView.Adapter<DsCatAdapter.DsCatViewHolder>{

    Context context;
    ArrayList<DsCatModel>dsCatModels;

    public DsCatAdapter (Context context, ArrayList<DsCatModel>dsCatModels){
        this.context = context;
        this.dsCatModels = dsCatModels;
    }


    @NonNull
    @Override
    public DsCatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ds_cat_item,null);

        return new DsCatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DsCatViewHolder holder, int position) {
        DsCatModel dsCatModel = dsCatModels.get(position);

        holder.lebel.setText(dsCatModel.getDsCatName());
        Glide.with(context)
                .load(dsCatModel.getDsCatIcon())
                .into(holder.dsCatIcon);
    }

    @Override
    public int getItemCount() {
        return dsCatModels.size();
    }

    public class DsCatViewHolder extends RecyclerView.ViewHolder{

        ImageView dsCatIcon;
        TextView lebel;

        public DsCatViewHolder(@NonNull View itemView) {
            super(itemView);
            dsCatIcon = itemView.findViewById(R.id.ds_cat_ico);
            lebel = itemView.findViewById(R.id.lebel);
        }
    }

}

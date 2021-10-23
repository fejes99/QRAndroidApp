package com.example.qrmenuapp.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.qrmenuapp.Model.ProductSelection;
import com.example.qrmenuapp.R;

import java.util.ArrayList;

public class ChildCartAdapter extends RecyclerView.Adapter<ChildCartAdapter.MyViewHolder> {

    Context context;
    public ArrayList<ProductSelection> productSelections;

    public ChildCartAdapter(Context context, ArrayList<ProductSelection> productSelections) {
        this.context = context;
        this.productSelections = productSelections;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.child_orders_items, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ProductSelection ps = productSelections.get(position);
        holder.productName.setText(ps.getProduct().getName());
        holder.productPrice.setText(String.valueOf(ps.getProduct().getPrice()));


        Glide.with(context)
                .load(ps.getProduct().getImage())
                .placeholder(R.drawable.ic_launcher_background)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority( Priority.IMMEDIATE )
                .dontAnimate()
                .into(holder.productImage);
    }

    @Override
    public int getItemCount() {
        return productSelections.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public ImageView productImage;
        public TextView productName;
        public TextView productPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
        }
    }
}
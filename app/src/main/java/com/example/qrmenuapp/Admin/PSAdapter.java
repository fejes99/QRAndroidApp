package com.example.qrmenuapp.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.qrmenuapp.Model.Product;
import com.example.qrmenuapp.R;

import java.util.ArrayList;

public class PSAdapter extends RecyclerView.Adapter<PSAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Product> productList;

    public PSAdapter(Context context, ArrayList<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public PSAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.psitem, parent, false);

        return new PSAdapter.ViewHolder(view, productList);
    }

    @Override
    public void onBindViewHolder(@NonNull PSAdapter.ViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.nameTV.setText(product.getName());
        holder.priceTV.setText(String.valueOf(product.getPrice()));

        Glide.with(context)
                .load(product.getImage())
                .placeholder(R.drawable.common_full_open_on_phone)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority( Priority.IMMEDIATE )
                .dontAnimate()
                .into(holder.foodIV);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageView foodIV;
        public TextView nameTV;
        public TextView priceTV;
        private ArrayList<Product> productList;

        public ViewHolder(@NonNull View itemView, ArrayList<Product> productList) {
            super(itemView);
            this.productList = productList;

            itemView.setOnClickListener(this);

            foodIV = (ImageView) itemView.findViewById(R.id.foodIV);
            nameTV = (TextView) itemView.findViewById(R.id.nameTV);
            priceTV = (TextView) itemView.findViewById(R.id.priceTV);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, "radi", Toast.LENGTH_SHORT).show();
        }
    }
}
package com.example.qrmenuapp.User;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.qrmenuapp.Model.Cart;
import com.example.qrmenuapp.Model.Product;
import com.example.qrmenuapp.Model.ProductSelection;
import com.example.qrmenuapp.R;

import java.util.ArrayList;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<Product> mProductArrayList;
    private Cart cart;

    public ProductAdapter(Context context, ArrayList<Product> productArrayList, Cart cart) {
        mContext = context;
        mProductArrayList = productArrayList;
        this.cart = cart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_user, parent, false);

        return new ViewHolder(view, mProductArrayList, this.cart);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = mProductArrayList.get(position);

        holder.fNameTextView.setText(product.getName());
        holder.fPriceTextView.setText(String.valueOf(product.getPrice()));

        Log.d("slika", product.getImage());

        Glide.with(mContext)
                .load(product.getImage())
                .placeholder(R.drawable.common_full_open_on_phone)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority( Priority.IMMEDIATE )
                .dontAnimate()
                .into(holder.foodImageView);
    }

    @Override
    public int getItemCount() {
        return mProductArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        public ImageView foodImageView;
        public TextView fNameTextView;
        public TextView fPriceTextView;
        public Button addToCartButton;
        private Cart cart;
        private ArrayList<Product> mProductArrayList;

        public ViewHolder(@NonNull View itemView, ArrayList<Product> products, Cart cart) {
            super(itemView);
            this.cart = cart;
            this.mProductArrayList = products;
            itemView.setOnClickListener(this);
            

            foodImageView = (ImageView) itemView.findViewById(R.id.foodImageViewUser);
            fNameTextView = (TextView) itemView.findViewById(R.id.fNameTextViewUser);
            fPriceTextView = (TextView) itemView.findViewById(R.id.fPriceTextViewUser);
            addToCartButton = (Button) itemView.findViewById(R.id.button_add);

            addToCartButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.button_add:
                    int position = getLayoutPosition();
                    Product product = this.mProductArrayList.get(position);
                    ProductSelection ps = new ProductSelection(product, 1);
                    this.cart.addSelectionToCart(ps);
                    break;
            }
        }
    }

}

package com.example.qrmenuapp.User;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.qrmenuapp.Model.Cart;
import com.example.qrmenuapp.Model.ProductSelection;
import com.example.qrmenuapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Context mContext;
    private List<ProductSelection> mProductSelectionList;
    private Cart cart;

    public CartAdapter(Context context, List<ProductSelection> productSelectionList, Cart cart) {
        mContext = context;
        mProductSelectionList = productSelectionList;
        this.cart = cart;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);

        return new ViewHolder(view, mProductSelectionList, cart);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProductSelection productSelection = mProductSelectionList.get(position);

        holder.cartNameTextView.setText(productSelection.getProduct().getName());
        holder.cartDescriptionTextView.setText(productSelection.getProduct().getDescription());
        holder.cartPriceTextView.setText(String.valueOf(productSelection.getTotalPrice()));
        holder.cartQuantityTextView.setText("Quantity: " + productSelection.getQuantity().toString());

        Glide.with(mContext)
                .load(productSelection.getProduct().getImage())
                .placeholder(R.drawable.common_full_open_on_phone)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .priority( Priority.IMMEDIATE )
                .dontAnimate()
                .into(holder.cartImageView);
    }

    @Override
    public int getItemCount() {
        return mProductSelectionList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView cartImageView;
        public TextView cartNameTextView;
        public TextView cartDescriptionTextView;
        public TextView cartPriceTextView;
        public TextView cartQuantityTextView;
        public Button cartIncreaseButton;
        public Button cartDecreaseButton;
        public ImageButton cartDeleteItemButton;

        private List<ProductSelection> productSelectionList;
        private Cart cart;

        public ViewHolder(@NonNull View itemView, List<ProductSelection> productSelection, Cart cart) {
            super(itemView);
            this.productSelectionList = cart.getProductSelections();
            this.cart = cart;
            itemView.setOnClickListener(this);

            cartImageView = (ImageView) itemView.findViewById(R.id.cartImageView);
            cartNameTextView = (TextView) itemView.findViewById(R.id.cartNameTextView);
            cartDescriptionTextView = (TextView) itemView.findViewById(R.id.cartDescriptionTextView);
            cartPriceTextView = (TextView) itemView.findViewById(R.id.cartPriceTextView);
            cartQuantityTextView = (TextView) itemView.findViewById(R.id.cartQuantityTextView);

            cartIncreaseButton = (Button) itemView.findViewById(R.id.increaseBtn);
            cartDecreaseButton = (Button) itemView.findViewById(R.id.decreaseBtn);
            cartDeleteItemButton = (ImageButton) itemView.findViewById(R.id.deleteItem);

            cartIncreaseButton.setOnClickListener(this);
            cartDecreaseButton.setOnClickListener(this);
            cartDeleteItemButton.setOnClickListener(this);
        }

        @RequiresApi(api = Build.VERSION_CODES.N)
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.increaseBtn:
                    int position = getLayoutPosition();
                    ProductSelection productSelection = productSelectionList.get(position);
                    String psId = productSelection.getProduct().getName();
                    productSelection.increaseQuantity();
                    updateCart(productSelection, psId);
                    cart.computeTotalPrice();
                    break;

                case R.id.decreaseBtn:
                    position = getLayoutPosition();
                    productSelection = productSelectionList.get(position);
                    psId = productSelection.getProduct().getName();
                    productSelection.decreaseQuantity();
                    cart.computeTotalPrice();
                    updateCart(productSelection, psId);
                    break;
                case R.id.deleteItem:
                    position = getLayoutPosition();
                    productSelection = productSelectionList.get(position);
                    psId = productSelection.getProduct().getName();
                    cart.removeSelectionFromCart(psId);
                    deleteFromCart(psId);
                    cart.computeTotalPrice();
                    break;

            }
        }
    }

    private void updateCart(ProductSelection ps, String psId) {
        DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference().child("cart");
        DatabaseReference dbPSRef = cartReference.child(cart.getCartId()).child("productSelections").child(psId);
        DatabaseReference dbTPRef = cartReference.child(cart.getCartId()).child("totalPrice");
        dbPSRef.setValue(ps);
        Log.d("price", String.valueOf(cart.getTotalPrice()));
        dbTPRef.setValue(cart.getTotalPrice());
    }

    private void deleteFromCart(String psId) {
        Log.d("psid", psId);
        DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference().child("cart");
        DatabaseReference dbPSRef = cartReference.child(cart.getCartId()).child("productSelections").child(psId);
        DatabaseReference dbTPRef = cartReference.child(cart.getCartId()).child("totalPrice");

        dbPSRef.setValue(null);
        dbTPRef.setValue(cart.getTotalPrice());
    }

}
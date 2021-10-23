package com.example.qrmenuapp.Admin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.qrmenuapp.Model.Product;
import com.example.qrmenuapp.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private Context mContext;
    private ArrayList<Product> mProductArrayList;

    private DatabaseReference mDatabase;

    public RecyclerAdapter(Context context, ArrayList<Product> productArrayList) {
        mContext = context;
        mProductArrayList = productArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.ViewHolder holder, int position) {
        Product product = mProductArrayList.get(position);

        holder.fNameTextView.setText(product.getName());
        holder.fDescriptionTextView.setText(product.getDescription());
        holder.fPriceTextView.setText(String.valueOf(product.getPrice()));

        Glide.with(mContext)
                .load(product.getImage())
                .into(holder.foodImageView);

//        holder.itemView.setOnClickListener(v -> {
//            Toast.makeText(mContext, "radi", Toast.LENGTH_SHORT).show();
//        });
    }

    @Override
    public int getItemCount() {
        return mProductArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView foodImageView;
        public TextView fNameTextView;
        public TextView fDescriptionTextView;
        public TextView fPriceTextView;
        public Button fUpdateButton;
        public Button fDeleteButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            foodImageView = (ImageView) itemView.findViewById(R.id.foodImageView);
            fNameTextView = (TextView) itemView.findViewById(R.id.fNameTextView);
            fDescriptionTextView = (TextView) itemView.findViewById(R.id.fDescriptionTextView);
            fPriceTextView = (TextView) itemView.findViewById(R.id.fPriceTextView);

            fUpdateButton = (Button) itemView.findViewById(R.id.updateBtn);
            fDeleteButton = (Button) itemView.findViewById(R.id.deleteBtn);

            fUpdateButton.setOnClickListener(this);
            fDeleteButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.updateBtn:
                    int position = getLayoutPosition();
                    Product product = mProductArrayList.get(position);
                    updateMenuItem(product);
                    break;

                case R.id.deleteBtn:
                    position = getLayoutPosition();
                    product = mProductArrayList.get(position);
                    deleteMenuItem(product);
                    break;
            }
        }

        public void updateMenuItem(Product product) {
            Intent intent = new Intent(mContext, UpdateProduct.class);
            intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("Menu", (Serializable) product);
            mContext.startActivity(intent);

            Toast.makeText(mContext, "Product updated", Toast.LENGTH_SHORT).show();
        }

        public void deleteMenuItem(Product product) {

            Log.d("imageRef", "Image ref: " + product.getImage());

//            mDatabase = FirebaseDatabase.getInstance().getReference();
//            FirebaseStorage storage = FirebaseStorage.getInstance();
//            StorageReference imageRef = storage.getReferenceFromUrl(product.getImage());

//            imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
//                @Override
//                public void onSuccess(Void aVoid) {
//                    Log.d("delete", "item deleted " + product.getImage());
//                    mDatabase.child("menu").child(product.getKey()).removeValue();
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Log.d("delete", "item not deleted ");
//                }
//            });
//            Log.d("delete", "item deleted " + id);

            mDatabase = FirebaseDatabase.getInstance().getReference();
            mDatabase.child("menu").child(product.getId()).removeValue();
        }
    }
}

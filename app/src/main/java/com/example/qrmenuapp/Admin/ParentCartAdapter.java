package com.example.qrmenuapp.Admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrmenuapp.Model.Cart;
import com.example.qrmenuapp.Model.Product;
import com.example.qrmenuapp.Model.ProductSelection;
import com.example.qrmenuapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ParentCartAdapter extends RecyclerView.Adapter<ParentCartAdapter.MyViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();

    Context context;
    public ArrayList<Cart> cartArrayList;

    public ParentCartAdapter(Context context, ArrayList<Cart> cartArrayList) {
        this.context = context;
        this.cartArrayList = cartArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.orders_items, parent, false);
        return new MyViewHolder(view, cartArrayList);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cart cart = cartArrayList.get(position);
        ArrayList<ProductSelection> ps = cart.getProductSelections();

        holder.cartTotalPrice.setText("Total price: " + String.valueOf(cart.getTotalPrice()));
        holder.cartTable.setText("Table number: " + cart.getTableNumber());
//        holder.cartProductNumber.setText(String.valueOf(cart.getProductSelections().size()));

//        LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
//        layoutManager.setInitialPrefetchItemCount(cart.getProductSelections().size());
//
//        ChildCartAdapter childCartAdapter = new ChildCartAdapter(holder.childRecyclerView.getContext(), ps);
//        holder.childRecyclerView.setLayoutManager(layoutManager);
//        holder.childRecyclerView.setAdapter(childCartAdapter);
//        holder.childRecyclerView.setRecycledViewPool(viewPool);
    }

    @Override
    public int getItemCount() {
        return cartArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private ArrayList<Cart> cartArrayList;
        public TextView cartTotalPrice;
        public TextView cartTable;
//        public TextView cartProductNumber;
        public RecyclerView childRecyclerView;
        private ArrayList<ProductSelection> productSelectionList;

        public MyViewHolder(@NonNull View itemView, ArrayList<Cart> cartArrayList) {
            super(itemView);
            this.cartArrayList = cartArrayList;
            cartTotalPrice = itemView.findViewById(R.id.cartTP);
            cartTable = itemView.findViewById(R.id.cartTable);
//            cartProductNumber = itemView.findViewById(R.id.cartpn);
            childRecyclerView = itemView.findViewById(R.id.Child_RV);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            Cart cart = cartArrayList.get(position);
            openPSforCart(cart);

//            startActivity(new Intent(context, .class));
            
        }

        private void openPSforCart(Cart cart) {
            String cartId = cart.getCartId();

            DatabaseReference firebase = FirebaseDatabase.getInstance().getReference();
            Query query = firebase.child("cart").child(cartId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    productSelectionList.clear();

                    for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String image = snapshot.child("product").child("image").getValue().toString();
                        String name = snapshot.child("product").child("name").getValue().toString();
                        String description = snapshot.child("product").child("description").getValue().toString();
                        Float price = Float.parseFloat(snapshot.child("product").child("price").getValue().toString());
                        String id = snapshot.child("product").getKey();
                        Integer quantity = Integer.parseInt(snapshot.child("quantity").getValue().toString());

                        Product product = new Product(name, description, price, image, id);
                        product.setId(id); // not sure do we need id but anyway

                        ProductSelection productSelection = new ProductSelection(new Product(name, description, price, image, id), quantity);

                        productSelectionList.add(productSelection);
                    }
                    getTotalPriceFromFirebase(cartId);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        public void getTotalPriceFromFirebase(String cartId) {
            Query query = FirebaseDatabase.getInstance().getReference().child("cart").child(cartId);
            query.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    cartTotalPrice.setText(snapshot.child("totalPrice").getValue().toString());
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
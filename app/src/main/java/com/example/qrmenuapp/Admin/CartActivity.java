package com.example.qrmenuapp.Admin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
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

public class CartActivity extends AppCompatActivity {
    private RecyclerView parentRecyclerView;
    private ParentCartAdapter ParentAdapter;

    private DatabaseReference mDatabase;

    private ArrayList<Cart> cartList;
    private ArrayList<ProductSelection> ps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart2);

        mDatabase = FirebaseDatabase.getInstance().getReference();

//        parentRecyclerView = (RecyclerView) findViewById(R.id.psRV);
        parentRecyclerView = (RecyclerView) findViewById(R.id.Parent_recyclerView);
        parentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cartList = new ArrayList<>();

        getDataFromFirebase();

        ParentAdapter = new ParentCartAdapter(getApplicationContext(), cartList);

        ParentAdapter = new ParentCartAdapter(getApplicationContext(), cartList);
        parentRecyclerView.setAdapter(ParentAdapter);
        ParentAdapter.notifyDataSetChanged();
    }

    private void getDataFromFirebase() {
        Query query = mDatabase.child("cart");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ps = new ArrayList<>();

                    String cartId = snapshot.getKey();
                    String tableNumber = (String) snapshot.child("tableNumber").getValue();
                    Boolean ordered = (Boolean) snapshot.child("ordered").getValue();
                    Boolean served = (Boolean) snapshot.child("served").getValue();
                    Float totalPrice = Float.parseFloat(snapshot.child("totalPrice").getValue().toString());

                    Long count = snapshot.child("productSelections").getChildrenCount();
                    for (DataSnapshot p : snapshot.child("productSelections").getChildren()) {


                        String image = p.child("product").child("image").getValue().toString();
                        String name = p.child("product").child("name").getValue().toString();
                        String description = p.child("product").child("description").getValue().toString();
                        Float price = Float.parseFloat(p.child("product").child("price").getValue().toString());
                        String id = p.child("product").child("id").getValue().toString();
                        Integer quantity = Integer.parseInt(p.child("quantity").getValue().toString());

                        Product product = new Product(name, description, price, image, id);

                        ProductSelection productSelection = new ProductSelection(product, quantity);

                        ps.add(productSelection);
                    }

                    Cart cart = new Cart(cartId, tableNumber, ordered, served);
                    cart.setTotalPrice(totalPrice);

                    cartList.add(cart);

                    ps.clear();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearAll() {
        if(cartList != null) {
            cartList.clear();

            if(ParentAdapter != null) {
                ParentAdapter.notifyDataSetChanged();
            }
        }
    }
}
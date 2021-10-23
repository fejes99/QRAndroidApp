package com.example.qrmenuapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.List;

public class CartActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;

    private RecyclerView mRecyclerView;
    private CartAdapter mCartAdapter;
    private Cart cart;

    private Button orderBtn;
    private TextView cartTotalPrice;

    private Float totalPrice;

    private List<ProductSelection> productSelectionList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        totalPrice = Float.parseFloat("0");

        mDatabase = FirebaseDatabase.getInstance().getReference();


        productSelectionList = new ArrayList<>();

        orderBtn = (Button) findViewById(R.id.btnOrder);
        cartTotalPrice = (TextView) findViewById(R.id.totalPrice);

        cart = (Cart) getIntent().getSerializableExtra("cart");
        String cartId = cart.getCartId();



        mRecyclerView = (RecyclerView) findViewById(R.id.cartRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        clearAll();

        getMenuDataFromFirebase(cartId);

        mCartAdapter = new CartAdapter(getApplicationContext(), productSelectionList, cart);
        mRecyclerView.setAdapter(mCartAdapter);
        mCartAdapter.notifyDataSetChanged();

        orderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.setOrdered(true);
                updateCart(cart);
                Intent intent = new Intent(getApplicationContext(), EndActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (!cart.isOrdered())
            deleteCart(cart.getCartId());
        finish();
    }

    private void deleteCart(String cartId) {
        DatabaseReference cartReference = mDatabase.child("cart");
        DatabaseReference dbCartRef = cartReference.child(cartId);

        dbCartRef.removeValue();
    }

    private void updateCart(Cart cart) {
        DatabaseReference cartReference = FirebaseDatabase.getInstance().getReference().child("cart");
        DatabaseReference dbOrderedRef = cartReference.child(cart.getCartId()).child("ordered");
        dbOrderedRef.setValue(cart.isOrdered());
    }

    private void clearAll() {
        if(productSelectionList != null) {
            productSelectionList.clear();

            if(mCartAdapter != null) {
                mCartAdapter.notifyDataSetChanged();
            }
        }
    }

    public void getTotalPriceFromFirebase(String cartId) {
        Query query = mDatabase.child("cart").child(cartId);
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

    public void getMenuDataFromFirebase(String cartId) {
        Query query = mDatabase.child("cart").child(cartId).child("productSelections");


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();

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
                Toast.makeText(getApplicationContext(),"Ne ucitava podatke", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
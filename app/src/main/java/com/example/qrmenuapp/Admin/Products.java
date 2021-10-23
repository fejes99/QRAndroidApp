package com.example.qrmenuapp.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrmenuapp.Model.Product;
import com.example.qrmenuapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Products extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;

    private DatabaseReference mDatabase;

    private ArrayList<Product> productArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_items);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        productArrayList = new ArrayList<>();

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseUser user = mAuth.getCurrentUser();
        if(user == null) {
            finish();
        }

        clearAll();

        getDataFromFirebase();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void getDataFromFirebase() {
        Query query = mDatabase.child("menu");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                clearAll();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    String image = snapshot.child("image").getValue().toString();
                    String name = snapshot.child("name").getValue().toString();
                    String description = snapshot.child("description").getValue().toString();
                    Float price = Float.parseFloat(snapshot.child("price").getValue().toString());
                    String id = snapshot.getKey();

                    Product product = new Product(name, description, price, image, id);
                    product.setId(id); // not sure do we need id but anyway

                    productArrayList.add(product);
                }

                mRecyclerAdapter = new RecyclerAdapter(getApplicationContext(), productArrayList);
                mRecyclerView.setAdapter(mRecyclerAdapter);
                mRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Toast.makeText(this,"Ne ucitava podatke", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearAll() {
        if(productArrayList != null) {
            productArrayList.clear();

            if(mRecyclerAdapter != null) {
                mRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    public void addItem(View view) {
        startActivity(new Intent(this, AddProduct.class));
    }
}
package com.example.qrmenuapp.User;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrmenuapp.Model.Cart;
import com.example.qrmenuapp.Model.Product;
import com.example.qrmenuapp.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ProductCatalog extends AppCompatActivity implements View.OnClickListener {

    private DatabaseReference mDatabase;

    private RecyclerView mRecyclerView;
    private ProductAdapter mRecyclerAdapter;

    private ImageButton cartBtn;

    private ArrayList<Product> mProductArrayList;
    private String tableNumber = "0";
    public Cart cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_catalog);

        Intent intent = getIntent();
        tableNumber = intent.getStringExtra("TableNumber");

        if(tableNumber.equals("0")) {
            Intent cameraIntent = new Intent(this, CameraActivity.class);
            startActivity(cameraIntent);
        }

        mDatabase = FirebaseDatabase.getInstance().getReference();

        mProductArrayList = new ArrayList<>();

        cartBtn = (ImageButton) findViewById(R.id.btnCart);
        cartBtn.setOnClickListener(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView1);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        clearAll();

        createCartIfNotExistFirebase(tableNumber);

        getMenuDataFromFirebase();


        mRecyclerAdapter = new ProductAdapter(getApplicationContext(), mProductArrayList, cart);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (cart.isOrdered() == false && cart.getProductSelections().size() < 1 && cart.getTotalPrice() == null)
            deleteCart(cart.getCartId());
        finish();
    }

    private void createCartIfNotExistFirebase(String tableNumber) {
        DatabaseReference cartReference = mDatabase.child("cart");
        DatabaseReference dbCartRef = cartReference.push();
        String cartId = dbCartRef.getKey();

        Date date = new Date();

        Map<String, Object> dataToSave = new HashMap<String, Object>();
        dataToSave.put("createdAt", date.toString());
        dataToSave.put("tableNumber", tableNumber);
        dataToSave.put("productSelections", new ArrayList());
        dataToSave.put("ordered", false);
        dataToSave.put("served", false);

        dbCartRef.setValue(dataToSave);

        cart = new Cart(cartId, tableNumber, date, new ArrayList<>(), false, false);
    }

    private void deleteCart(String cartId) {
        Log.d("cartId", cartId);
        DatabaseReference cartReference = mDatabase.child("cart");
        DatabaseReference dbCartRef = cartReference.child(cartId);

        dbCartRef.removeValue();
    }

    private void getMenuDataFromFirebase() {
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
                    mProductArrayList.add(product);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Ne ucitava podatke", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clearAll() {
        if(mProductArrayList != null) {
            mProductArrayList.clear();

            if(mRecyclerAdapter != null) {
                mRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onClick(View v) {
        DatabaseReference cartReference = mDatabase.child("cart");
        DatabaseReference dbCartRef = cartReference.child(cart.getCartId());

        for(int i = 0; i < cart.getProductSelections().size(); i++) {
            dbCartRef.child("productSelections").child(cart.getProductSelections().get(i).getProduct().getName()).setValue(cart.getProductSelections().get(i));
        }
        dbCartRef.child("totalPrice").setValue(String.valueOf(cart.getTotalPrice()));

        Intent intentCart = new Intent(this, CartActivity.class);
        intentCart.putExtra("cart", (Serializable) cart);
        startActivity(intentCart);
    }
//
//    public static class ProductItem extends AppCompatActivity {
//
//        private Context mContext;
//
//        private ImageView mPicFood;
//        private TextView mNameFood;
//        private TextView mDescriptionFood;
//        private TextView mPriceFood;
//        private Button mAddToChart;
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            setContentView(R.layout.activity_product_item);
//
//
//            mContext = getApplicationContext();
//
//            Product product = (Product) getIntent().getSerializableExtra("Menu");
//
//            mNameFood = (TextView) findViewById(R.id.productName);
//            mDescriptionFood = (TextView) findViewById(R.id.productDescription);
//            mPriceFood = (TextView) findViewById(R.id.productPrice);
//            mPicFood = (ImageView) findViewById(R.id.productImageView);
//            mAddToChart = findViewById(R.id.productAddToChart);
//
//            mNameFood.setText(product.getName(), TextView.BufferType.EDITABLE);
//            mDescriptionFood.setText(product.getDescription(), TextView.BufferType.EDITABLE);
//            mPriceFood.setText(String.valueOf(product.getPrice()), TextView.BufferType.EDITABLE);
//
//            Glide
//                    .with(mContext)
//                    .load(product.getImage())
//                    .into(mPicFood);
//
//            mAddToChart.setOnClickListener(v -> {
//                finish();
//            });
//        }
//    }
}
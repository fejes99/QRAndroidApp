package com.example.qrmenuapp.Admin;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.qrmenuapp.Model.Product;
import com.example.qrmenuapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class UpdateProduct extends AppCompatActivity {

    public static final String NAME_KEY = "name";
    public static final String DESCRIPTION_KEY = "description";
    public static final String PRICE_KEY = "price";
    public static final String IMAGE_KEY = "image";
    private static final int GALLERY_CODE = 1;
    private Context mContext;

    private ImageView mPicFood;
    private EditText mNameFood;
    private EditText mDescriptionFood;
    private EditText mPriceFood;
    private Button mUpdateBtn;
    private Uri mImageUri;

    private String key;

    private DatabaseReference mDatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_menu_item);

        mContext = this.getApplicationContext();

        Product product = (Product) getIntent().getSerializableExtra("Menu");
        key = product.getKey();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("menu");
        mStorage = FirebaseStorage.getInstance().getReference();

        mNameFood = (EditText) findViewById(R.id.updateFoodName);
        mDescriptionFood = (EditText) findViewById(R.id.updateFoodDescription);
        mPriceFood = (EditText) findViewById(R.id.updateFoodPrice);
        mPicFood = (ImageView) findViewById(R.id.updateFoodImage);
        mUpdateBtn = (Button) findViewById(R.id.updateItemBtn);


        mNameFood.setText(product.getName(), TextView.BufferType.EDITABLE);
        mDescriptionFood.setText(product.getDescription(), TextView.BufferType.EDITABLE);
        mPriceFood.setText(String.valueOf(product.getPrice()), TextView.BufferType.EDITABLE);

        mPicFood.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_CODE);
        });


        Glide
                .with(mContext)
                .load(product.getImage())
                .into(mPicFood);

        mUpdateBtn.setOnClickListener(v -> {
            updateData(key);
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == GALLERY_CODE && resultCode == RESULT_OK && data != null) {
            mImageUri = data.getData();
            mPicFood.setImageURI(mImageUri);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                mContext.getContentResolver().takePersistableUriPermission(mImageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            }
        }
    };

    public void updateData(String key) {
        final String nameFoodValue = mNameFood.getText().toString().trim();
        final String descriptionFoodValue = mDescriptionFood.getText().toString().trim();
        final String priceFoodValue = mPriceFood.getText().toString().trim();
        final String imageFoodValue = mImageUri.toString();

        if(nameFoodValue.isEmpty() || descriptionFoodValue.isEmpty() || priceFoodValue.isEmpty()) { return; }


        final StorageReference filepath = mStorage.child("Food_image").child(mImageUri.getLastPathSegment());

        UploadTask uploadTask = filepath.putFile(mImageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Log.i("ChildKey", key);

                DatabaseReference updateItem = mDatabase.child(key);

                Map<String, Object> dataToSave = new HashMap<String, Object>();
                dataToSave.put(NAME_KEY, nameFoodValue);
                dataToSave.put(DESCRIPTION_KEY, descriptionFoodValue);
                dataToSave.put(PRICE_KEY, priceFoodValue);
                dataToSave.put(IMAGE_KEY, imageFoodValue);

                updateItem.updateChildren(dataToSave);

                Toast.makeText(mContext, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                finish();
            }
        });
    }
}
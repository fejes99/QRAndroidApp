package com.example.qrmenuapp.Admin;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrmenuapp.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.Map;

public class AddProduct extends AppCompatActivity {

    public static final String NAME_KEY = "name";
    public static final String DESCRIPTION_KEY = "description";
    public static final String PRICE_KEY = "price";
    public static final String IMAGE_KEY = "image";
    public static final String IMAGE_URI = "imageUri";
    private static final int GALLERY_CODE = 1;

    private ImageView mFoodImage;
    private EditText mNameFood;
    private EditText mDescriptionFood;
    private EditText mPriceFood;
    private Button mSaveBtn;
    private Uri mImageUri;

    private DatabaseReference menuDatabase;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_menu_item);

        menuDatabase = FirebaseDatabase.getInstance().getReference().child("menu");
        mStorage = FirebaseStorage.getInstance().getReference();


        mNameFood = (EditText) findViewById(R.id.editFoodName);
        mDescriptionFood = (EditText) findViewById(R.id.editFoodDescription);
        mPriceFood = (EditText) findViewById(R.id.editFoodPrice);
        mFoodImage = (ImageButton) findViewById(R.id.editFoodImage);
        mSaveBtn = (Button) findViewById(R.id.saveBtn);

        mFoodImage.setOnClickListener(v -> {
            Intent galleryIntent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            galleryIntent.setType("image/*");
            startActivityForResult(galleryIntent, GALLERY_CODE);
        });

        mSaveBtn.setOnClickListener(v -> {
            saveData();
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
            mFoodImage.setImageURI(mImageUri);
        }
    }

    public void saveData() {
        final String nameFoodValue = mNameFood.getText().toString().trim();
        final String descriptionFoodValue = mDescriptionFood.getText().toString().trim();
        final String priceFoodValue = mPriceFood.getText().toString().trim();
        final String imageFoodValue = mImageUri.toString().trim();

        if(nameFoodValue.isEmpty() || descriptionFoodValue.isEmpty() || priceFoodValue.isEmpty()) { return; }

        final StorageReference filepath = mStorage.child("Food_image").child(mImageUri.getLastPathSegment());

        UploadTask uploadTask = filepath.putFile(mImageUri);
        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AddProduct.this, "Image uploaded successfully", Toast.LENGTH_SHORT).show();

                DatabaseReference dbMenuRef = menuDatabase.push();

                Map<String, Object> dataToSave = new HashMap<String, Object>();
                dataToSave.put(NAME_KEY, nameFoodValue);
                dataToSave.put(DESCRIPTION_KEY, descriptionFoodValue);
                dataToSave.put(PRICE_KEY, priceFoodValue);
                dataToSave.put(IMAGE_KEY, imageFoodValue);

                dbMenuRef.setValue(dataToSave);
                finish();
            }
        });
    }
}
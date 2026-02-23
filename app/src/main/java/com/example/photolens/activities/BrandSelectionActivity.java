package com.example.photolens.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.photolens.R;
import com.example.photolens.utils.SharedPrefsHelper;

public class BrandSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brand_selection);

        CardView canonCard = findViewById(R.id.canon_card);
        CardView sonyCard = findViewById(R.id.sony_card);
        CardView nikonCard = findViewById(R.id.nikon_card);

        canonCard.setOnClickListener(v -> selectBrand("canon"));
        sonyCard.setOnClickListener(v -> selectBrand("sony"));
        nikonCard.setOnClickListener(v -> selectBrand("nikon"));
    }

    private void selectBrand(String brand) {
        SharedPrefsHelper.saveBrand(this, brand);
        startActivity(new Intent(this, PhotoSelectionActivity.class));
    }
}
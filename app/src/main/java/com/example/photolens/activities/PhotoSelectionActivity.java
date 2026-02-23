package com.example.photolens.activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.example.photolens.R;
import com.example.photolens.utils.ImageEffectsHelper;
import com.example.photolens.utils.ImageHolder;

import java.io.IOException;
import java.io.InputStream;

public class PhotoSelectionActivity extends AppCompatActivity {

    private ActivityResultLauncher<String> requestPermissionLauncher;
    private ActivityResultLauncher<Intent> pickImageLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_selection);

        Button galleryButton = findViewById(R.id.gallery_button);
        Button demoButton = findViewById(R.id.demo_button);

        requestPermissionLauncher = registerForActivityResult(
                new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    if (isGranted) {
                        openGallery();
                    } else {
                        Toast.makeText(this, "Требуется разрешение на доступ к галерее", Toast.LENGTH_SHORT).show();
                    }
                });

        pickImageLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        Uri uri = result.getData().getData();
                        try {
                            Bitmap bitmap = loadBitmapFromUri(uri);
                            Bitmap scaled = scaleBitmap(bitmap);

                            Bitmap finalBitmapForProcessing = ImageEffectsHelper.ensureMutableSoftwareBitmap(scaled);

                            ImageHolder.setImage(finalBitmapForProcessing); // Сохраняем гарантированно мутабельный битмап

                            // Освобождаем промежуточный 'scaled' битмап, если он больше не нужен
                            if (scaled != finalBitmapForProcessing && !scaled.isRecycled()) {
                                scaled.recycle();
                            }

                            startActivity(new Intent(this, CameraSimulatorActivity.class));
                            finish(); // Закрываем этот экран
                        } catch (IOException e) {
                            e.printStackTrace();
                            Toast.makeText(this, "Ошибка загрузки изображения", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        galleryButton.setOnClickListener(v -> {
            // Проверяем версию Android для правильного разрешения
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissionLauncher.launch(Manifest.permission.READ_MEDIA_IMAGES);
            } else {
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE);
            }
        });

        demoButton.setOnClickListener(v -> {
            try {
                InputStream is = getAssets().open("demo_photo.jpg");
                Bitmap bitmap = BitmapFactory.decodeStream(is); // Этот битмап из assets обычно мутабельный и программный
                Bitmap scaled = scaleBitmap(bitmap);
                is.close();

                Bitmap finalBitmapForProcessing = ImageEffectsHelper.ensureMutableSoftwareBitmap(scaled);

                ImageHolder.setImage(finalBitmapForProcessing); // Сохраняем гарантированно мутабельный битмап

                // Освобождаем промежуточный 'scaled' битмап, если он больше не нужен
                if (scaled != finalBitmapForProcessing && !scaled.isRecycled()) {
                    scaled.recycle();
                }

                startActivity(new Intent(this, CameraSimulatorActivity.class));
                finish();
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Ошибка загрузки демо-фото", Toast.LENGTH_SHORT).show();
            }
        });

        // Back button
        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickImageLauncher.launch(intent);
    }

    // Исправленный метод загрузки битмап
    private Bitmap loadBitmapFromUri(Uri uri) throws IOException {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            ImageDecoder.Source source = ImageDecoder.createSource(getContentResolver(), uri);
            return ImageDecoder.decodeBitmap(source);
        } else {
            return MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
        }
    }

    private Bitmap scaleBitmap(Bitmap original) {
        if (original == null) return null; // Обработка null

        int maxWidth = 1280;
        if (original.getWidth() > maxWidth) {
            float ratio = (float) maxWidth / original.getWidth();
            int height = (int) (original.getHeight() * ratio);
            Bitmap scaled = Bitmap.createScaledBitmap(original, maxWidth, height, true);
            if (scaled != original) { // Проверяем, был ли создан новый битмап
                original.recycle(); // Освобождаем оригинал, если он больше не нужен
            }
            return scaled;
        }
        return original; // Если не масштабировался, возвращаем оригинал
    }
}
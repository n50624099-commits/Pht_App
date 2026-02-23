package com.example.photolens.activities;

import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.photolens.R;
import com.example.photolens.models.CameraParameters;
import com.example.photolens.utils.ImageEffectsHelper;
import com.example.photolens.utils.ImageHolder;
import com.example.photolens.utils.ImageSaver;
import com.example.photolens.utils.SharedPrefsHelper;

public class CameraSimulatorActivity extends AppCompatActivity {

    private Bitmap originalBitmap;
    private Bitmap processedBitmap; // Сохраняем обработанное изображение (результат применения эффектов)
    private ImageView previewImageView;
    private ProgressBar progressBar;
    private TextView exposureText;
    private CameraParameters params;
    private Button isoButton, apertureButton, shutterButton, resetButton, saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_simulator);

        previewImageView = findViewById(R.id.preview_image);
        progressBar = findViewById(R.id.progress_bar);
        exposureText = findViewById(R.id.exposure_text);
        isoButton = findViewById(R.id.iso_button);
        apertureButton = findViewById(R.id.aperture_button);
        shutterButton = findViewById(R.id.shutter_button);
        resetButton = findViewById(R.id.reset_button);
        saveButton = findViewById(R.id.save_button);

        originalBitmap = ImageHolder.getImage();
        if (originalBitmap == null) {
            Toast.makeText(this, "Изображение не загружено", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        params = new CameraParameters();

        // Apply brand styling
        String brand = SharedPrefsHelper.getSelectedBrand(this);
        int accentColor = SharedPrefsHelper.getBrandColor(this);

        // Применяем полупрозрачный акцентный цвет к панели
        View parametersPanel = findViewById(R.id.parameters_panel);
        parametersPanel.setBackgroundColor(Color.argb(50, Color.red(accentColor),
                Color.green(accentColor), Color.blue(accentColor)));

        updateButtons();
        applyAllEffects();

        isoButton.setOnClickListener(v -> showPickerDialog(
                "Выберите ISO",
                intArrayToStringArray(CameraParameters.ISO_VALUES),
                params.getIsoIndex(),
                index -> {
                    params.setIsoIndex(index);
                    updateButtons();
                    applyAllEffects();
                }
        ));

        apertureButton.setOnClickListener(v -> showPickerDialog(
                "Выберите диафрагму",
                doubleArrayToStringArray(CameraParameters.APERTURE_VALUES),
                params.getApertureIndex(),
                index -> {
                    params.setApertureIndex(index);
                    updateButtons();
                    applyAllEffects();
                }
        ));

        shutterButton.setOnClickListener(v -> showPickerDialog(
                "Выберите выдержку",
                CameraParameters.SHUTTER_VALUES,
                params.getShutterIndex(),
                index -> {
                    params.setShutterIndex(index);
                    updateButtons();
                    applyAllEffects();
                }
        ));

        resetButton.setOnClickListener(v -> {
            params.reset();
            updateButtons();
            applyAllEffects();
        });

        saveButton.setOnClickListener(v -> {
            if (processedBitmap != null) {
                // processedBitmap должен быть ARGB_8888 после применения эффектов
                ImageSaver.saveToGallery(this, processedBitmap);
            } else {
                Toast.makeText(this, "Нет изображения для сохранения", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.back_button).setOnClickListener(v -> finish());
    }

    private void showPickerDialog(String title, String[] values, int currentIndex, PickerCallback callback) {
        NumberPicker picker = new NumberPicker(this);
        picker.setMinValue(0);
        picker.setMaxValue(values.length - 1);
        picker.setDisplayedValues(values);
        picker.setValue(currentIndex);
        picker.setWrapSelectorWheel(false);

        new AlertDialog.Builder(this)
                .setTitle(title)
                .setView(picker)
                .setPositiveButton("Применить", (dialog, which) -> callback.onSelected(picker.getValue()))
                .setNegativeButton("Отмена", null)
                .show();
    }

    private interface PickerCallback {
        void onSelected(int index);
    }

    private void updateButtons() {
        isoButton.setText("ISO: " + CameraParameters.ISO_VALUES[params.getIsoIndex()]);
        apertureButton.setText("f/" + CameraParameters.APERTURE_VALUES[params.getApertureIndex()]);
        shutterButton.setText(CameraParameters.SHUTTER_VALUES[params.getShutterIndex()]);
    }

    private void applyAllEffects() {
        progressBar.setVisibility(View.VISIBLE);

        new Thread(() -> {
            Bitmap currentWorkingBitmap = originalBitmap.copy(Bitmap.Config.ARGB_8888, true); // Это строка 149 теперь

            // Noise (ISO)
            int noiseLevel = calculateNoiseLevel(params.getIsoIndex());
            if (noiseLevel > 0) {
                currentWorkingBitmap = ImageEffectsHelper.applyNoise(currentWorkingBitmap, noiseLevel);
            }

            // Blur (Aperture) - отключен для производительности, можно включить
            /*int blurRadius = calculateBlurRadius(params.getApertureIndex());
            if (blurRadius > 0) {
                currentWorkingBitmap = ImageEffectsHelper.applyBlur(currentWorkingBitmap, blurRadius);
            }*/

            // Brightness (Shutter + Exposure)
            int exposureValue = params.calculateExposure();
            float brightness = calculateBrightness(params.getShutterIndex(), exposureValue);
            currentWorkingBitmap = ImageEffectsHelper.adjustBrightness(currentWorkingBitmap, brightness);

            Bitmap finalProcessed = currentWorkingBitmap;
            runOnUiThread(() -> {
                // Освобождаем предыдущий processedBitmap ПЕРЕД установкой нового,
                // чтобы освободить память от старого результата эффектов.
                // Проверяем, что это не originalBitmap, чтобы не освободить оригинал, который нужен для сброса.
                if (processedBitmap != null && processedBitmap != originalBitmap && !processedBitmap.isRecycled()) {
                    processedBitmap.recycle();
                }

                previewImageView.setImageBitmap(finalProcessed);
                processedBitmap = finalProcessed; // Сохраняем для кнопки "Сохранить"
                progressBar.setVisibility(View.GONE);
                updateExposureIndicator(exposureValue);
            });

            runOnUiThread(() -> {
                if (isFinishing()) return; // ПРОВЕРКА: если экран закрывается, ничего не делаем

                if (processedBitmap != null && processedBitmap != originalBitmap && !processedBitmap.isRecycled()) {
                    processedBitmap.recycle();
                }
                previewImageView.setImageBitmap(finalProcessed);
                processedBitmap = finalProcessed;
                progressBar.setVisibility(View.GONE);
                updateExposureIndicator(exposureValue);
            });
        }).start();

    }

    private int calculateNoiseLevel(int isoIndex) {
        if (isoIndex <= 2) return 5; // 100-400
        else if (isoIndex <= 4) return 15; // 800-1600
        else return 30; // 3200-6400
    }

    private int calculateBlurRadius(int apertureIndex) {
        if (apertureIndex <= 1) return 8; // f/1.8-2.8
        else if (apertureIndex <= 3) return 4; // f/4-5.6
        else if (apertureIndex <= 5) return 2; // f/8-11
        return 0; // f/16
    }

    private float calculateBrightness(int shutterIndex, int exposureValue) {
        float brightness = 0f;
        if (shutterIndex >= 7) brightness = 50f; // 1"-10"
        else if (shutterIndex >= 5) brightness = 25f; // 1/30-1/15

        float exposureCorrection = 0f;
        if (exposureValue < 3) exposureCorrection = -30f;
        else if (exposureValue > 8) exposureCorrection = 30f;

        return brightness + exposureCorrection;
    }

    private void updateExposureIndicator(int exposureValue) {
        String status = params.getExposureStatus();
        exposureText.setText(status);
        int color = status.equals("Норма") ? Color.GREEN : Color.RED;
        exposureText.setTextColor(color);
    }

    private String[] intArrayToStringArray(int[] values) {
        String[] strings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            strings[i] = String.valueOf(values[i]);
        }
        return strings;
    }

    private String[] doubleArrayToStringArray(double[] values) {
        String[] strings = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            strings[i] = String.valueOf(values[i]);
        }
        return strings;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Освобождаем processedBitmap, если он существует, отличается от originalBitmap и еще не был освобожден.
        if (processedBitmap != null && processedBitmap != originalBitmap && !processedBitmap.isRecycled()) {
            processedBitmap.recycle();
        }
        // ImageHolder.clear() позаботится об освобождении originalBitmap.
        ImageHolder.clear();
    }

}
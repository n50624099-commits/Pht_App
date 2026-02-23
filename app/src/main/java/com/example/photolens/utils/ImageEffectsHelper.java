package com.example.photolens.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;

import java.util.Random;

public class ImageEffectsHelper {


    public static Bitmap ensureMutableSoftwareBitmap(Bitmap source) { // Изменено на public
        if (source == null) {
            throw new IllegalArgumentException("Source bitmap cannot be null.");
        }

        if (source.getConfig() == Bitmap.Config.HARDWARE || !source.isMutable()) {

            return source.copy(Bitmap.Config.ARGB_8888, true);
        } else {
            return source.copy(source.getConfig(), true);
        }
    }


    public static Bitmap applyNoise(Bitmap source, int noiseLevel) {

        Bitmap result = ensureMutableSoftwareBitmap(source);

        int width = result.getWidth();
        int height = result.getHeight();
        Random random = new Random();
        int step = 2; // оптимизация, обработка каждого 2 пикселя

        // работа с массивом пикселей
        int[] pixels = new int[width * height];
        result.getPixels(pixels, 0, width, 0, 0, width, height);

        for (int x = 0; x < width; x += step) {
            for (int y = 0; y < height; y += step) {
                int index = y * width + x;
                int pixel = pixels[index];

                int r = Color.red(pixel);
                int g = Color.green(pixel);
                int b = Color.blue(pixel);

                int noise = random.nextInt(noiseLevel * 2) - noiseLevel;
                r = Math.max(0, Math.min(255, r + noise));
                g = Math.max(0, Math.min(255, g + noise));
                b = Math.max(0, Math.min(255, b + noise));

                int newColor = Color.rgb(r, g, b);

                // Применяем эффект к блоку пикселей для оптимизации и распространения шума
                for (int dx = 0; dx < step && x + dx < width; dx++) {
                    for (int dy = 0; dy < step && y + dy < height; dy++) {
                        pixels[(y + dy) * width + (x + dx)] = newColor;
                    }
                }
            }
        }
        result.setPixels(pixels, 0, width, 0, 0, width, height); // Записываем пиксели обратно
        return result;
    }

    public static Bitmap applyBlur(Bitmap source, int radius) {
        if (radius < 1) return source;

        Bitmap processedSource = ensureMutableSoftwareBitmap(source);

        int width = processedSource.getWidth();
        int height = processedSource.getHeight();

        Bitmap result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        //подумать над другой реализацией для производительности с getPixels/setPixels/fast Blur
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int r = 0, g = 0, b = 0, count = 0;
                for (int dy = -radius; dy <= radius; dy++) {
                    for (int dx = -radius; dx <= radius; dx++) {
                        int nx = Math.max(0, Math.min(width - 1, x + dx));
                        int ny = Math.max(0, Math.min(height - 1, y + dy));
                        int pixel = processedSource.getPixel(nx, ny);
                        r += Color.red(pixel);
                        g += Color.green(pixel);
                        b += Color.blue(pixel);
                        count++;
                    }
                }
                r /= count;
                g /= count;
                b /= count;
                result.setPixel(x, y, Color.rgb(r, g, b));
            }
        }
        // Освобождаем временный processedSource, если это была копия и он больше не нужен
        if (processedSource != source && !processedSource.isRecycled()) {
            processedSource.recycle();
        }

        return result;
    }

    public static Bitmap adjustBrightness(Bitmap source, float brightnessValue) {
        ColorMatrix colorMatrix = new ColorMatrix();
        colorMatrix.set(new float[]{
                1, 0, 0, 0, brightnessValue,
                0, 1, 0, 0, brightnessValue,
                0, 0, 1, 0, brightnessValue,
                0, 0, 0, 1, 0
        });

        Bitmap result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(result);
        Paint paint = new Paint();
        paint.setColorFilter(new ColorMatrixColorFilter(colorMatrix));
        canvas.drawBitmap(source, 0, 0, paint);
        return result;
    }
}
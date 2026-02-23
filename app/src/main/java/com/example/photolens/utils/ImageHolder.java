package com.example.photolens.utils;

import android.graphics.Bitmap;

public class ImageHolder {
    private static Bitmap currentImage;

    public static void setImage(Bitmap bitmap) {
        currentImage = bitmap;
    }

    public static Bitmap getImage() {
        return currentImage;
    }

    public static void clear() {
        if (currentImage != null && !currentImage.isRecycled()) {
            currentImage.recycle();
            currentImage = null;
        }
    }
}

package com.example.pixelpost.Utils.SupportClass;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;

public class BitmapToByte {

    /**
     * Convert Bitmap to byte array.
     *
     * @param bitmap  The Bitmap to be converted.
     * @param format  The format of the image (e.g., Bitmap.CompressFormat.PNG or Bitmap.CompressFormat.JPEG).
     * @param quality The quality of the compressed image (0-100).
     * @return The byte array representing the image.
     */
    public static byte[] convertBitmapToByteArray(Bitmap bitmap, Bitmap.CompressFormat format, int quality) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(format, quality, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }
}
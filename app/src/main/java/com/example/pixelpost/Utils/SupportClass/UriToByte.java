package com.example.pixelpost.Utils.SupportClass;

import android.content.ContentResolver;
import android.net.Uri;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class UriToByte {
    public static byte[] convertUriToByteArray(ContentResolver contentResolver, Uri uri) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        InputStream inputStream = null;

        try {
            inputStream = contentResolver.openInputStream(uri);
            if (inputStream == null) {
                return null;
            }

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            return byteArrayOutputStream.toByteArray();

        } catch (IOException e) {
            Log.e("UriToByteArrayConverter", "Error converting URI to byte array", e);
            return null;
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
                byteArrayOutputStream.close();
            } catch (IOException e) {
                Log.e("UriToByteArrayConverter", "Error closing streams", e);
            }
        }
    }
}

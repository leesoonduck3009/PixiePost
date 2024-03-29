package com.example.pixelpost.Utils.SupportClass;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class PasswordUtils {

    // Hàm hash mật khẩu
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedBytes);
        } catch (NoSuchAlgorithmException e) {
            Log.e("hash_password_log",e.getMessage());
            return null;
        }
    }

    // Hàm giải hash mật khẩu (không thể giải ngược hash)
    public static boolean verifyPassword(String inputPassword, String hashedPassword) {
        String hashedInputPassword = hashPassword(inputPassword);
        return hashedInputPassword != null && hashedInputPassword.equals(hashedPassword);
    }
}
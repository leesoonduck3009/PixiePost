package com.example.pixelpost.Utils.SupportClass;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class OTPGenerator {
    private static final int OTP_LENGTH = 4;
    private static final long OTP_VALID_DURATION = TimeUnit.MINUTES.toMillis(2); // Thời hạn của OTP là 2 phút
    private static long OTP_EXPIRY_TIME = System.currentTimeMillis() + OTP_VALID_DURATION;
    public static String generateOTP() {
        StringBuilder otp = new StringBuilder();
        Random random = new Random();
        OTP_EXPIRY_TIME = System.currentTimeMillis() + OTP_VALID_DURATION;
        for (int i = 0; i < OTP_LENGTH; i++) {
            otp.append(random.nextInt(10)); // Tạo một chữ số ngẫu nhiên từ 0 đến 9 và thêm vào chuỗi OTP
        }
        return otp.toString();
    }

    public static boolean isOTPValid(String otpInput, String otpGenerate) {
        return otpInput.equals(otpGenerate) && System.currentTimeMillis() <= OTP_EXPIRY_TIME;
    }
}

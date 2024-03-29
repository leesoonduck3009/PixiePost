package com.example.pixelpost.Utils.SupportClass;

import java.util.regex.Pattern;

public class ValidateData {
    public static boolean isValidEmail(String email) {
        if (!email.isEmpty())
        {
            // Email address validation pattern
            String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";

            // Create a Pattern object with the emailPattern
            Pattern pattern = Pattern.compile(emailPattern);

            // Use the Matcher class to match the pattern against the email string
            return pattern.matcher(email).matches();
        }
        else
            return false;
    }
}
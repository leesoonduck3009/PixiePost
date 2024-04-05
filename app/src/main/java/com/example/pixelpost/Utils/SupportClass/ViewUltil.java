package com.example.pixelpost.Utils.SupportClass;

import android.view.View;

public class ViewUltil {
    public static boolean isPointInsideView(View view, float pointX, float pointY) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        float imageViewX = location[0];
        float imageViewY = location[1];
        float imageViewWidth = view.getWidth();
        float imageViewHeight = view.getHeight();

        if (imageViewX <= pointX && pointX <= imageViewX + imageViewWidth &&
                imageViewY <= pointY && pointY <= imageViewY + imageViewHeight) {
            return true;
        } else {
            return false;
        }
    }
}

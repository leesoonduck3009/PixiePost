package com.example.pixelpost.Utils.SupportClass;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.Log;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
;import java.util.HashMap;
import java.util.Map;

public class QRCode {
    public static final String QRCODE_STRING_GENERATED = "pixel-post: ";
    public static Bitmap generateQRwithLogo(Bitmap qrCodeBitmap, Bitmap logoBitmap) {
        Bitmap overlayBitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth(), qrCodeBitmap.getHeight(), qrCodeBitmap.getConfig());
        Canvas canvas = new Canvas(overlayBitmap);

        // Vẽ mã QR lên bitmap mới
        canvas.drawBitmap(qrCodeBitmap, 0, 0, null);

        // Tính toán vị trí và kích thước của logo để nó nằm giữa hình tròn
        int logoX = (qrCodeBitmap.getWidth() - logoBitmap.getWidth()) / 2 + 3;
        int logoY = (qrCodeBitmap.getHeight() - logoBitmap.getHeight()) / 2 + 12;

        // Vẽ thêm các yếu tố đồ họa khác nếu cần
        Paint paint = new Paint();
        paint.setColor(Color.WHITE); // Màu của hình tròn
        paint.setStyle(Paint.Style.FILL_AND_STROKE);

        // Vẽ hình tròn lớn ở giữa để tạo nền cho logo
        float centerX = (float) qrCodeBitmap.getWidth() / 2;
        float centerY = (float) qrCodeBitmap.getHeight() / 2;
        float radius = (float) logoBitmap.getWidth() / 2; // Bán kính của hình tròn lớn, thêm 10 để đảm bảo logo nằm trong hình tròn
        // Vẽ hình tròn lớn
        canvas.drawCircle(centerX, centerY, radius, paint);

        // Vẽ logo lên trên mã QR, đã căn chỉnh để nằm giữa hình tròn
        canvas.drawBitmap(logoBitmap, logoX, logoY, null);

        // Vẽ các hình tròn nhỏ ở góc nếu cần
        // ...

        return overlayBitmap;
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int cornerRadius) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = cornerRadius;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    public static Bitmap generateQRCode(String content, int width, int height) {
        try {
            // Tạo MultiFormatWriter để tạo mã QR
            Map<EncodeHintType, Object> hints = new HashMap<>();
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H); // Sử dụng mức độ sửa lỗi cao
// Các cài đặt khác cho mã QR
            hints.put(EncodeHintType.MARGIN, 1); // Đặt lề cho mã QR

// Tạo mã QR với các hints đã chỉ định
            BitMatrix bitMatrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            // Tạo BitMatrix từ nội dung


            // Chuyển BitMatrix sang Bitmap
            Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);

            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    bitmap.setPixel(i, j, bitMatrix.get(i, j) ? Color.BLACK : Color.WHITE);
                }
            }

            return bitmap;
        } catch (Exception e) {
            Log.e("qr-code-generated", e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

}

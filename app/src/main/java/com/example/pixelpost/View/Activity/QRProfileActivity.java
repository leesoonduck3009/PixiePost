package com.example.pixelpost.View.Activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.pixelpost.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.encoder.BarcodeMatrix;

import java.nio.ByteBuffer;

public class QRProfileActivity extends AppCompatActivity {
    private ImageView qrCodeImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qrprofile);
        String dataToEncode = "https://example.com";

        // Đường dẫn đến hình ảnh logo của bạn
        int logoResourceId = R.drawable.image_icon1; // Thay thế bằng ID của hình ảnh logo trong thư mục res/drawable

        // Tạo mã QR với logo
        qrCodeImageView = findViewById(R.id.qrCodeImage);
        Bitmap qrCodeBitmap = generateQRCodeWithLogo(dataToEncode, logoResourceId);
        qrCodeImageView.setImageBitmap(qrCodeBitmap);
    }
    private Bitmap generateQRCodeWithLogo(String data, int logoResourceId) {
        try {
            MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
            BitMatrix bitMatrix = multiFormatWriter.encode(data, BarcodeFormat.QR_CODE, 500, 500);

            // Tạo hình ảnh từ BitMatrix
            int width = bitMatrix.getWidth();
            int height = bitMatrix.getHeight();
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    pixels[y * width + x] = bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF;
                }
            }
            Bitmap qrCodeBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            qrCodeBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

            // Tải hình ảnh logo
            Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), logoResourceId);

            // Tính toán vị trí để đặt logo vào giữa mã QR
            int logoPositionX = (qrCodeBitmap.getWidth() - logoBitmap.getWidth()) / 2;
            int logoPositionY = (qrCodeBitmap.getHeight() - logoBitmap.getHeight()) / 2;

            // Dán logo lên mã QR
// Dán logo lên mã QR
            Bitmap finalBitmap = Bitmap.createBitmap(qrCodeBitmap.getWidth(), qrCodeBitmap.getHeight(), qrCodeBitmap.getConfig());
            finalBitmap.setPixels(pixels, 0, width, 0, 0, width, height);

// Tạo một buffer cho qrCodeBitmap và logoBitmap
            int qrCodeBufferSize = qrCodeBitmap.getRowBytes() * qrCodeBitmap.getHeight();
            ByteBuffer qrCodeBuffer = ByteBuffer.allocate(qrCodeBufferSize);
            qrCodeBitmap.copyPixelsToBuffer(qrCodeBuffer);

            int logoBufferSize = logoBitmap.getRowBytes() * logoBitmap.getHeight();
            ByteBuffer logoBuffer = ByteBuffer.allocate(logoBufferSize);
            logoBitmap.copyPixelsToBuffer(logoBuffer);

// Sao chép dữ liệu từ buffer vào finalBitmap
            finalBitmap.copyPixelsFromBuffer(qrCodeBuffer);
            finalBitmap.copyPixelsFromBuffer(logoBuffer);
            for (int y = 0; y < logoBitmap.getHeight(); y++) {
                for (int x = 0; x < logoBitmap.getWidth(); x++) {
                    int pixel = logoBitmap.getPixel(x, y);
                    if (pixel != 0) {
                        finalBitmap.setPixel(logoPositionX + x, logoPositionY + y, pixel);
                    }
                }
            }
            return finalBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}

package com.example.pixelpost.View.Activity.QR;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pixelpost.R;
import com.example.pixelpost.Utils.SupportClass.QRCode;
import com.example.pixelpost.View.Activity.Login.Login01Activity;
import com.example.pixelpost.databinding.ActivityQrprofileBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class QRProfileActivity extends AppCompatActivity {
    private ImageView imgQR;
    private ImageView btnScanQR;
    private ImageView btnBackMenu;
    private ActivityQrprofileBinding binding;
    Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_qrprofile);

        // Đường dẫn đến hình ảnh logo của bạn
        int logoResourceId = R.drawable.image_icon1; // Thay thế bằng ID của hình ảnh logo trong thư mục res/drawable

        // Tạo mã QR với logo
        imgQR = findViewById(R.id.imgQR);
        btnScanQR = findViewById(R.id.btnScanQR);
        btnBackMenu = findViewById(R.id.btnBackMenu);
        imgQR.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                createQRCode();
            }
        });
        setListener();
    }
    private void setListener()
    {
        btnBackMenu.setOnClickListener(v->finish());
        btnScanQR.setOnClickListener(v->{
            startActivity(new Intent(getApplicationContext(), QrScannerActivity.class));
            finish();
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
    }

    public void createQRCode(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null)
        {
            try {
                Log.d("qr-code-width",String.valueOf(imgQR.getWidth()));
                Log.d("qr-code-height",String.valueOf(imgQR.getHeight()));

                Bitmap qrCodeBitmap = QRCode.generateQRCode(QRCode.QRCODE_STRING_GENERATED + FirebaseAuth.getInstance().getCurrentUser().getUid(),imgQR.getWidth(), imgQR.getHeight());
                // Tải hình ảnh
                Bitmap logoBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.image_icon1_resize  );
                assert qrCodeBitmap != null;
                qrCodeBitmap = QRCode.getRoundedCornerBitmap(qrCodeBitmap,30);
                Bitmap combineLogo = QRCode.generateQRwithLogo(qrCodeBitmap,logoBitmap);
                imgQR.setImageBitmap(combineLogo);
            } catch (Exception e) {
                Log.e("bit-map",e.getMessage());
                e.printStackTrace();

            }
        }
        else
        {
            Intent intent = new Intent(getApplicationContext(), Login01Activity.class);
            startActivity(intent);
            finish();
        }
    }

}


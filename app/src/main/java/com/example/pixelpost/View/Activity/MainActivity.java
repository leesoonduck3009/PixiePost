package com.example.pixelpost.View.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.pixelpost.R;

public class MainActivity extends AppCompatActivity {
    Button btnMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnMessage = findViewById(R.id.btnMessage);
        btnMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ConversationListActivity.class);
                startActivity(intent);
            }
        });
    }
}
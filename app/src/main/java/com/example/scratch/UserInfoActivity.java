package com.example.scratch;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UserInfoActivity extends AppCompatActivity {
    private Button userInfo_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_information);
        userInfo_button = findViewById(R.id.userInfoButton);
        userInfo_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserInfoActivity.this, DisplayUsageActivity.class);
                startActivity(intent);
            }
        });
    }
}
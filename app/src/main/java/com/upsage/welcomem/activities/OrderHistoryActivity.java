package com.upsage.welcomem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.upsage.welcomem.R;
import com.upsage.welcomem.utils.ThemeUtil;

public class OrderHistoryActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_history);
        //todo finish this
    }
}

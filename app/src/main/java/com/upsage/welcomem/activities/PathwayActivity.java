package com.upsage.welcomem.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.upsage.welcomem.R;
import com.upsage.welcomem.utils.ThemeUtil;

public class PathwayActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pathway);
        //todo finish this
    }
}

package com.upsage.welcomem.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.upsage.welcomem.R;
import com.upsage.welcomem.utils.SQLSingleton;
import com.upsage.welcomem.utils.ThemeStyle;
import com.upsage.welcomem.utils.ThemeUtil;

public class SettingsActivity extends AppCompatActivity {

    RadioButton darkRadioButton;
    RadioButton lightRadioButton;
    RadioButton largeRadioButton;
    RadioButton normalRadioButton;
    RadioButton smallRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtil.onCreateSetTheme(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        darkRadioButton = findViewById(R.id.darkRadioButton);
        lightRadioButton = findViewById(R.id.lightRadioButton);
        largeRadioButton = findViewById(R.id.largeRadioButton);
        normalRadioButton = findViewById(R.id.normalRadioButton);
        smallRadioButton = findViewById(R.id.smallRadioButton);

        switch(ThemeUtil.getStyle()){
            case LightLarge:
                lightRadioButton.setChecked(true);
                darkRadioButton.setChecked(false);
                largeRadioButton.setChecked(true);
                normalRadioButton.setChecked(false);
                smallRadioButton.setChecked(false);
                break;
            case LightSmall:
                lightRadioButton.setChecked(true);
                darkRadioButton.setChecked(false);
                largeRadioButton.setChecked(false);
                normalRadioButton.setChecked(false);
                smallRadioButton.setChecked(true);
                break;
            case LightNormal:
                lightRadioButton.setChecked(true);
                darkRadioButton.setChecked(false);
                largeRadioButton.setChecked(false);
                normalRadioButton.setChecked(true);
                smallRadioButton.setChecked(false);
                break;
            case BlackLarge:
                lightRadioButton.setChecked(false);
                darkRadioButton.setChecked(true);
                largeRadioButton.setChecked(true);
                normalRadioButton.setChecked(false);
                smallRadioButton.setChecked(false);
                break;
            case BlackSmall:
                lightRadioButton.setChecked(false);
                darkRadioButton.setChecked(true);
                largeRadioButton.setChecked(false);
                normalRadioButton.setChecked(false);
                smallRadioButton.setChecked(true);
                break;
            case BlackNormal:
                lightRadioButton.setChecked(false);
                darkRadioButton.setChecked(true);
                largeRadioButton.setChecked(false);
                normalRadioButton.setChecked(true);
                smallRadioButton.setChecked(false);
                break;
        }
    }

    @Override
    protected void onResume() {
        SQLSingleton.startConnection();
        super.onResume();
    }


    void changeTheme(ThemeStyle style) {
        ThemeUtil.changeToTheme(this, style);
    }

    public void applySettings(View view) {
        if (darkRadioButton.isChecked()) {

            if (largeRadioButton.isChecked())
                changeTheme(ThemeStyle.BlackLarge);

            else if (normalRadioButton.isChecked())
                changeTheme(ThemeStyle.BlackNormal);

            else
                changeTheme(ThemeStyle.BlackSmall);

        } else {

            if (largeRadioButton.isChecked())
                changeTheme(ThemeStyle.LightLarge);

            else if (normalRadioButton.isChecked())
                changeTheme(ThemeStyle.LightNormal);

            else
                changeTheme(ThemeStyle.LightSmall);
        }

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(this,MainActivity.class));
        finish();
    }
}

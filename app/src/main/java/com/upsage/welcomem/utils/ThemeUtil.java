package com.upsage.welcomem.utils;

import android.content.Intent;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;

import com.upsage.welcomem.R;

public class ThemeUtil {
    static private ThemeStyle style;
    static private SharedPreferences themePreferences;

    public static void onCreateSetTheme(AppCompatActivity activity){
        if(themePreferences==null) {
            themePreferences = activity.getSharedPreferences("theme", 0);
            style = ThemeStyle.valueOf(
                    themePreferences.getInt(
                            "style",
                            ThemeStyle.LightNormal.getValue()));
        }
        switch (style){
            case BlackNormal:
                activity.setTheme(R.style.Dark);
                break;
            case BlackSmall:
                activity.setTheme(R.style.Dark_Small);
                break;
            case BlackLarge:
                activity.setTheme(R.style.Dark_Large);
                break;
            case LightNormal:
                activity.setTheme(R.style.Light);
                break;
            case LightSmall:
                activity.setTheme(R.style.Light_Small);
                break;
            case LightLarge:
                activity.setTheme(R.style.Light_Large);
                break;
                default:
        }
    }

    public static ThemeStyle getStyle() {
        return style;
    }

    public static void changeToTheme(AppCompatActivity activity,ThemeStyle style) {
        if(themePreferences==null)
            themePreferences = activity.getSharedPreferences("theme",0);

        ThemeUtil.style = style;

        themePreferences.edit().putInt("style",style.getValue()).apply();

        Intent intent = activity.getIntent();
        activity.finish();
        activity.startActivity(intent);

    }
}

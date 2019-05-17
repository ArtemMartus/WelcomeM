package com.upsage.welcomem.utils;

import java.util.HashMap;
import java.util.Map;

public enum ThemeStyle {
    BlackNormal(0),
    BlackSmall(1),
    BlackLarge(2),
    LightNormal(3),
    LightSmall(4),
    LightLarge(5);


    private int value;
    private static Map map = new HashMap<>();

    ThemeStyle(int value) {
        this.value = value;
    }

    static {
        for (ThemeStyle pageType : ThemeStyle.values()) {
            map.put(pageType.value, pageType);
        }
    }

    public static ThemeStyle valueOf(int pageType) {
        return (ThemeStyle) map.get(pageType);
    }

    public int getValue() {
        return value;
    }

}

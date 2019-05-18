package com.upsage.welcomem.interfaces;

import android.content.Context;

public interface DatabasePojo extends OnTaskCompleted {
    boolean load(Context context);

    void save();

    void copy(Object o);

    void test(OnTaskCompleted receiver);
}

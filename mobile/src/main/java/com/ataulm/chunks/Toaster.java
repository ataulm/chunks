package com.ataulm.chunks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.Toast;

public class Toaster {

    @SuppressLint("StaticFieldLeak") // application context so no activity leak & don't care about instant run
    private static Context context;
    private static Toast toast;

    static void init(Context context) {
        Toaster.context = context.getApplicationContext();
    }

    public static void show(String message) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }

}

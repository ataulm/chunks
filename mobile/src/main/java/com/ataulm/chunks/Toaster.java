package com.ataulm.chunks;

import android.content.Context;
import android.widget.Toast;

public class Toaster {

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

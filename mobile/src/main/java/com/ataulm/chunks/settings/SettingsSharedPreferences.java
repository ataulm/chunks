package com.ataulm.chunks.settings;

import android.content.Context;
import android.content.SharedPreferences;

class SettingsSharedPreferences {

    private static final String KEY_AUTO_ADVANCE_ITEMS = "auto_advance_items";
    private static final String KEY_AUTO_CLEAR_COMPLETED = "auto_clear_completed";

    private final SharedPreferences sharedPreferences;

    public static SettingsSharedPreferences create(Context context) {
        return new SettingsSharedPreferences(context.getSharedPreferences("settings", Context.MODE_PRIVATE));
    }

    SettingsSharedPreferences(SharedPreferences sharedPreferences) {
        this.sharedPreferences = sharedPreferences;
    }

    public boolean shouldAutoAdvanceItemsFromTomorrow() {
        return sharedPreferences.getBoolean(KEY_AUTO_ADVANCE_ITEMS, true);
    }

    public void setAutoAdvanceItemFromTomorrow(boolean shouldAutoAdvance) {
        sharedPreferences.edit()
                .putBoolean(KEY_AUTO_ADVANCE_ITEMS, shouldAutoAdvance)
                .apply();
    }

    public boolean shouldAutoClearCompletedItemsFromToday() {
        return sharedPreferences.getBoolean(KEY_AUTO_CLEAR_COMPLETED, true);
    }

    public void setAutoClearCompletedItemsFromToday(boolean shouldAutoClear) {
        sharedPreferences.edit()
                .putBoolean(KEY_AUTO_CLEAR_COMPLETED, shouldAutoClear)
                .apply();
    }
}

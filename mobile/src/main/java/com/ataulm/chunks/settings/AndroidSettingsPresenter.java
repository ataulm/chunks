package com.ataulm.chunks.settings;

import android.support.annotation.IdRes;

import com.ataulm.chunks.R;
import com.ataulm.chunks.Toaster;
import com.ataulm.settings.HeadingItem;
import com.ataulm.settings.Settings;
import com.ataulm.settings.SwitchItem;

import static com.ataulm.settings.SwitchItem.State.OFF;
import static com.ataulm.settings.SwitchItem.State.ON;

public class AndroidSettingsPresenter implements SettingsPresenter {

    private final SettingsView settingsView;
    private final SettingsSharedPreferences sharedPreferences;

    public AndroidSettingsPresenter(SettingsView settingsView, SettingsSharedPreferences sharedPreferences) {
        this.settingsView = settingsView;
        this.sharedPreferences = sharedPreferences;
    }

    @Override
    public void startPresenting() {
        Settings settings = readSettingsFromSharedPreferences();
        settingsView.display(settings);
    }

    private Settings readSettingsFromSharedPreferences() {
        HeadingItem headingItem = autoHeading();
        Settings.Item autoAdvanceFromTomorrow = autoAdvanceFromTomorrow();
        Settings.Item autoClearCompletedTasksFromToday = autoClearCompletedTask();

        return new Settings.Builder()
                .add(headingItem, autoAdvanceFromTomorrow, autoClearCompletedTasksFromToday)
                .build();
    }

    private HeadingItem autoHeading() {
        return new HeadingItem(R.id.chunks_settings_heading_automation, "Automation");
    }

    private Settings.Item autoAdvanceFromTomorrow() {
        return createSwitchItem(R.id.chunks_settings_auto_advance_from_tomorrow, "Auto advance tomorrow", new SwitchItem.Callback() {
            @Override
            public void onToggleSwitchItem(SwitchItem item) {
                Toaster.show("toggled switch on item: " + item);
                SwitchItem.State state = item.state();
                boolean newState = state != ON;
                sharedPreferences.setAutoAdvanceItemFromTomorrow(newState);
            }
        }, sharedPreferences.shouldAutoAdvanceItemsFromTomorrow());
    }

    private Settings.Item autoClearCompletedTask() {
        SwitchItem.Callback callback = new SwitchItem.Callback() {
            @Override
            public void onToggleSwitchItem(SwitchItem item) {
                Toaster.show("toggled switch on item: " + item);
                SwitchItem.State state = item.state();
                boolean newState = state != ON;
                sharedPreferences.setAutoClearCompletedItemsFromToday(newState);
            }
        };
        boolean shouldAutoClearCompletedItemsFromToday = sharedPreferences.shouldAutoClearCompletedItemsFromToday();
        return createSwitchItem(
                R.id.chunks_settings_auto_clear_completed_from_today,
                "Auto clear today's",
                callback,
                shouldAutoClearCompletedItemsFromToday
        );
    }

    private Settings.Item createSwitchItem(@IdRes final int id, String title, SwitchItem.Callback callback, boolean on) {
        return new SwitchItem(
                id,
                title,
                true,
                on ? ON : OFF,
                callback
        );
    }

    @Override
    public void stopPresenting() {
        // no op? any op?
    }

}

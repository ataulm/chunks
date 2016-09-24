package com.ataulm.chunks.settings;

import android.os.Bundle;

import com.ataulm.chunks.BaseActivity;
import com.ataulm.chunks.R;

import butterknife.ButterKnife;

public class SettingsActivity extends BaseActivity {

    private SettingsPresenter settingsPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SettingsView settingsView = ButterKnife.findById(this, R.id.settings_screen);
        SettingsSharedPreferences settingsSharedPreferences = SettingsSharedPreferences.create(this);
        settingsPresenter = new AndroidSettingsPresenter(settingsView, settingsSharedPreferences);
    }

    @Override
    protected void onResume() {
        super.onResume();
        settingsPresenter.startPresenting();
    }

    @Override
    protected void onPause() {
        settingsPresenter.stopPresenting();
        super.onPause();
    }

}

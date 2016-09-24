package com.ataulm.chunks.settings;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.ataulm.settings.Settings;
import com.ataulm.settings.SettingsAdapter;
import com.ataulm.settings.StandardSettingsViewHolderFactory;

public class SettingsScreen extends RecyclerView implements SettingsView {

    public SettingsScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @Override
    public void display(Settings settings) {
        SettingsAdapter settingsAdapter = new SettingsAdapter(new StandardSettingsViewHolderFactory());
        settingsAdapter.updateSettings(settings);

        setAdapter(settingsAdapter);
    }

}

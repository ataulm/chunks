package com.ataulm.settings;

import android.view.ViewGroup;

import com.ataulm.chunks.R;

public class StandardSettingsViewHolderFactory implements SettingsViewHolderFactory {

    @Override
    public boolean supportsViewType(int viewType) {
        return viewType == R.id.settings__view_type_switch;
    }

    @Override
    public SettingsViewHolder createViewHolder(ViewGroup parent, int viewType) {
        if (viewType == R.id.settings__view_type_heading) {
            return HeadingViewHolder.inflate(parent);
        }

        if (viewType == R.id.settings__view_type_switch) {
            return SwitchViewHolder.inflate(parent);
        }

        throw new IllegalArgumentException("Unsupported view type: " + viewType);
    }

}

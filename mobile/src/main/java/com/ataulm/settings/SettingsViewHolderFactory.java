package com.ataulm.settings;

import android.view.ViewGroup;

public interface SettingsViewHolderFactory {

    boolean supportsViewType(int viewType);

    SettingsViewHolder createViewHolder(ViewGroup parent, int viewType);

}

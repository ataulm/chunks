package com.ataulm.settings;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class SettingsViewHolder extends RecyclerView.ViewHolder {

    protected SettingsViewHolder(View itemView) {
        super(itemView);
    }

    public abstract void bind(Settings.Item item);

}

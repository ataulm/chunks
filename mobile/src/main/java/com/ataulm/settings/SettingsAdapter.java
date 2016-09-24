package com.ataulm.settings;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class SettingsAdapter extends RecyclerView.Adapter<SettingsViewHolder> {

    private final SettingsViewHolderFactory settingsViewHolderFactory;

    private Settings settings;

    public SettingsAdapter(StandardSettingsViewHolderFactory settingsViewHolderFactory) {
        this.settingsViewHolderFactory = settingsViewHolderFactory;
    }

    public void updateSettings(Settings settings) {
        this.settings = settings;
        notifyDataSetChanged();
    }

    @Override
    public SettingsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return settingsViewHolderFactory.createViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(SettingsViewHolder holder, int position) {
        Settings.Item item = settings.get(position);
        holder.bind(item);
    }

    @Override
    public int getItemViewType(int position) {
        return settings.get(position).viewType();
    }

    @Override
    public int getItemCount() {
        return settings == null ? 0 : settings.size();
    }

}

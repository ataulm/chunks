package com.ataulm.settings;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ataulm.chunks.R;

public class HeadingViewHolder extends SettingsViewHolder {

    public static HeadingViewHolder inflate(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        TextView view = (TextView) inflater.inflate(R.layout.settings__view_heading, parent, false);
        return new HeadingViewHolder(view);
    }

    public HeadingViewHolder(TextView view) {
        super(view);
    }

    @Override
    public void bind(Settings.Item item) {
        bind(((HeadingItem) item));
    }

    private void bind(HeadingItem entry) {
        ((TextView) itemView).setText(entry.title());
    }
}

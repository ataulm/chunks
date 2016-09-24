package com.ataulm.settings;

import com.ataulm.chunks.R;

public class HeadingItem implements Settings.Item {

    private final int id;
    private final CharSequence title;

    public HeadingItem(int id, CharSequence title) {
        this.id = id;
        this.title = title;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public boolean enabled() {
        return false;
    }

    @Override
    public int viewType() {
        return R.id.settings__view_type_heading;
    }

    @Override
    public CharSequence title() {
        return title;
    }
}

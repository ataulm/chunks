package com.ataulm.settings;

import android.support.annotation.IdRes;

import com.ataulm.chunks.R;

public class SwitchItem implements Settings.Item {

    @IdRes
    private final int id;
    private final CharSequence title;
    private final boolean enabled;
    private final State state;
    private final Callback callback;

    public SwitchItem(@IdRes int id, CharSequence title, boolean enabled, State state, Callback callback) {
        this.id = id;
        this.title = title;
        this.enabled = enabled;
        this.state = state;
        this.callback = callback;
    }

    @Override
    public int id() {
        return id;
    }

    @Override
    public boolean enabled() {
        return enabled;
    }

    @Override
    public int viewType() {
        return R.id.settings__view_type_switch;
    }

    @Override
    public CharSequence title() {
        return title;
    }

    public State state() {
        return state;
    }

    public void onToggle() {
        callback.onToggleSwitchItem(this);
    }

    public void onClick() {
        callback.onClickSwitchItem(this);
    }

    public void onLongClick() {
        callback.onLongClickSwitchItem(this);
    }

    public enum State {

        ON,
        OFF

    }

    public static abstract class Callback {

        public void onClickSwitchItem(SwitchItem item) {
            onToggleSwitchItem(item);
        }

        public void onLongClickSwitchItem(SwitchItem item) {
            // no op by default
        }

        public abstract void onToggleSwitchItem(SwitchItem item);

    }

}

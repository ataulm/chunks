package com.ataulm.settings;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.ataulm.chunks.R;

import static com.ataulm.settings.SwitchItem.State.ON;

public class SwitchViewHolder extends SettingsViewHolder {

    private final TextView titleTextView;
    private final Switch switchView;

    public static SwitchViewHolder inflate(ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.settings__view_switch, parent, false);
        TextView titleTextView = (TextView) view.findViewById(R.id.settings__switch_text_title);
        Switch switchView = (Switch) view.findViewById(R.id.settings__switch);
        return new SwitchViewHolder(view, titleTextView, switchView);
    }

    public SwitchViewHolder(View view, TextView titleTextView, Switch switchView) {
        super(view);
        this.titleTextView = titleTextView;
        this.switchView = switchView;
    }

    @Override
    public void bind(Settings.Item item) {
        bind(((SwitchItem) item));
    }

    private void bind(SwitchItem entry) {
        titleTextView.setText(entry.title());
        itemView.setEnabled(entry.enabled());
        bindCheckState(entry, switchView);
        bindClickListener(entry);
        bindLongClickListener(entry);
    }

    private void bindCheckState(final SwitchItem entry, Switch switchView) {
        switchView.setOnCheckedChangeListener(null);
        switchView.setChecked(entry.state() == ON);
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                entry.onToggle();
            }
        });
    }

    private void bindLongClickListener(final SwitchItem entry) {
        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                entry.onLongClick();
                return true;
            }
        });
    }

    private void bindClickListener(final SwitchItem entry) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                entry.onClick();
            }
        });
    }

}

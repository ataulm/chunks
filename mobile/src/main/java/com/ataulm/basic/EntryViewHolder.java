package com.ataulm.basic;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

final class EntryViewHolder extends RecyclerView.ViewHolder {

    public final EntryView entryView;

    public static EntryViewHolder inflate(ViewGroup parent) {
        EntryView view = (EntryView) LayoutInflater.from(parent.getContext()).inflate(R.layout.view_entry, parent, false);
        return new EntryViewHolder(view);
    }

    private EntryViewHolder(EntryView entryView) {
        super(entryView);
        this.entryView = entryView;
    }

}

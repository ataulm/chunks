package com.ataulm.chunks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

final class EntryViewHolder extends RecyclerView.ViewHolder {

    final EntryWidget entryWidget;

    public static EntryViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        EntryWidget view = (EntryWidget) layoutInflater.inflate(R.layout.view_entry, parent, false);
        return new EntryViewHolder(view);
    }

    private EntryViewHolder(EntryWidget entryWidget) {
        super(entryWidget);
        this.entryWidget = entryWidget;
    }

}

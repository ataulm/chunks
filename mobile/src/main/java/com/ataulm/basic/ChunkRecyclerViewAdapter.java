package com.ataulm.basic;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public class ChunkRecyclerViewAdapter extends RecyclerView.Adapter<EntryViewHolder> {

    private Chunk chunk;

    public ChunkRecyclerViewAdapter(Chunk chunk) {
        this.chunk = chunk;
    }

    public void update(Chunk chunk) {
        this.chunk = chunk;
        notifyDataSetChanged();
    }

    @Override
    public EntryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return EntryViewHolder.inflate(parent);
    }

    @Override
    public void onBindViewHolder(EntryViewHolder holder, int position) {
        Entry entry = chunk.get(position);
        holder.entryView.bind(entry);
    }

    @Override
    public int getItemCount() {
        return chunk.size();
    }

    @Override
    public long getItemId(int position) {
        Entry entry = chunk.get(position);
        return entry.id().value().hashCode();
    }

}

package com.ataulm.chunks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class ChunkRecyclerViewAdapter extends RecyclerView.Adapter<EntryViewHolder> {

    private final ChunkEntryUserInteractions userInteractions;

    private Chunk chunk;

    public ChunkRecyclerViewAdapter(ChunkEntryUserInteractions userInteractions, Chunk chunk) {
        this.userInteractions = userInteractions;
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
        holder.entryWidget.bind(entry, userInteractions);
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

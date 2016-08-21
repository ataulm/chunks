package com.ataulm.chunks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

public class ChunkRecyclerViewAdapter extends RecyclerView.Adapter<EntryViewHolder> {

    private final ChunkEntryUserInteractions userInteractions;

    private Day day;
    private Chunk chunk;

    public ChunkRecyclerViewAdapter(ChunkEntryUserInteractions userInteractions, Day day, Chunk chunk) {
        this.userInteractions = userInteractions;
        this.day = day;
        this.chunk = chunk;
    }

    public void update(Day day, Chunk chunk) {
        this.day = day;
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
        holder.entryWidget.bind(day, entry, userInteractions);
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

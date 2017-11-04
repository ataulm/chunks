package com.ataulm.chunks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ChunkRecyclerViewAdapter extends RecyclerView.Adapter<ChunkItemViewHolder> {

    private final ChunkItemUserInteractions userInteractions;

    private Day day;
    private Chunk chunk;

    ChunkRecyclerViewAdapter(ChunkItemUserInteractions userInteractions, Day day, Chunk chunk) {
        this.userInteractions = userInteractions;
        this.day = day;
        this.chunk = chunk;
        super.setHasStableIds(true);
    }

    boolean update(Day day, Chunk chunk) {
        boolean addedItem = chunk.size() > this.chunk.size();

        this.day = day;
        this.chunk = chunk;
        notifyDataSetChanged();

        return addedItem;
    }

    void onItemMoving(int source, int target) {
        List<Item> entries = new ArrayList<>(chunk.entries());
        if (source < target) {
            for (int i = source; i < target; i++) {
                Collections.swap(entries, i, i + 1);
            }
        } else {
            for (int i = source; i > target; i--) {
                Collections.swap(entries, i, i - 1);
            }
        }
        this.chunk = Chunk.create(entries);
        notifyItemMoved(source, target);
    }

    void onItemMoved(int endPosition) {
        Item item = chunk.get(endPosition);
        userInteractions.onUserMove(item, endPosition);
    }

    @Override
    public ChunkItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ChunkItemViewHolder.inflate(parent);
    }

    @Override
    public void onBindViewHolder(ChunkItemViewHolder holder, int position) {
        Item item = chunk.get(position);
        ChunksActions chunksActions = ChunksActions.create(chunk, day, item, userInteractions);
        holder.bind(item, chunksActions);
    }

    @Override
    public int getItemCount() {
        return chunk.size();
    }

    @Override
    public long getItemId(int position) {
        Item item = chunk.get(position);
        return item.id().value().hashCode();
    }

}

package com.ataulm.chunks;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class ChunkRecyclerViewAdapter extends RecyclerView.Adapter<ItemViewHolder> {

    private final ItemUserInteractions userInteractions;
    private final ItemViewHolder.DragStartListener dragStartListener;

    private Day day;
    private Items items;

    ChunkRecyclerViewAdapter(ItemUserInteractions userInteractions,
                             ItemViewHolder.DragStartListener dragStartListener,
                             Day day,
                             Items items) {
        this.userInteractions = userInteractions;
        this.dragStartListener = dragStartListener;
        this.day = day;
        this.items = items;

        super.setHasStableIds(true);
    }

    boolean update(Day day, Items items) {
        boolean addedItem = items.size() > this.items.size();

        this.day = day;
        this.items = items;
        notifyDataSetChanged();

        return addedItem;
    }

    void onItemMoving(int source, int target) {
        List<Item> entries = new ArrayList<>(items.entries());
        if (source < target) {
            for (int i = source; i < target; i++) {
                Collections.swap(entries, i, i + 1);
            }
        } else {
            for (int i = source; i > target; i--) {
                Collections.swap(entries, i, i - 1);
            }
        }
        this.items = Items.create(entries);
        notifyItemMoved(source, target);
    }

    void onItemMoved(int endPosition) {
        Item item = items.get(endPosition);
        userInteractions.onUserMove(item, endPosition);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return ItemViewHolder.inflate(parent);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {
        Item item = items.get(position);
        ChunksActions chunksActions = ChunksActions.create(items, day, item, userInteractions);
        holder.bind(item, chunksActions, dragStartListener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public long getItemId(int position) {
        Item item = items.get(position);
        return item.id().value().hashCode();
    }

}

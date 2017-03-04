package com.ataulm.chunks;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

final class ChunkItemViewHolder extends RecyclerView.ViewHolder {

    final ChunkItemView chunkItemView;

    public static ChunkItemViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ChunkItemView view = (ChunkItemView) layoutInflater.inflate(R.layout.view_chunk_item, parent, false);
        return new ChunkItemViewHolder(view);
    }

    private ChunkItemViewHolder(ChunkItemView chunkItemView) {
        super(chunkItemView);
        this.chunkItemView = chunkItemView;
    }

}

package com.ataulm.chunks;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.novoda.viewpageradapter.ViewPagerAdapter;

final class ChunksPagerAdapter extends ViewPagerAdapter<RecyclerView> {

    private final EntryView.UserInteractions userInteractions;
    private Chunks chunks;

    ChunksPagerAdapter(EntryView.UserInteractions userInteractions, Chunks chunks) {
        this.userInteractions = userInteractions;
        this.chunks = chunks;
    }

    public void update(Chunks chunks) {
        this.chunks = chunks;
        notifyDataSetChanged();
    }

    @Override
    protected RecyclerView createView(ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        RecyclerView recyclerView = (RecyclerView) layoutInflater.inflate(R.layout.view_entries_page, container, false);
        recyclerView.setLayoutManager(new LinearLayoutManager(container.getContext()));
        return recyclerView;
    }

    @Override
    protected void bindView(RecyclerView view, int position) {
        Chunk chunk = getChunkForPosition(position);

        RecyclerView.Adapter adapter = view.getAdapter();
        if (adapter == null) {
            view.setAdapter(new ChunkRecyclerViewAdapter(userInteractions, chunk));
        } else {
            ((ChunkRecyclerViewAdapter) adapter).update(chunk);
        }
    }

    private Chunk getChunkForPosition(int position) {
        if (position == 0) {
            return chunks.yesterday();
        }

        if (position == 1) {
            return chunks.today();
        }

        if (position == 2) {
            return chunks.tomorrow();
        }

        throw new IllegalArgumentException("no idea what to do for position " + position);
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Yesterday";
        }

        if (position == 1) {
            return "Today";
        }

        if (position == 2) {
            return "Tomorrow";
        }

        throw new IllegalArgumentException("no idea what to do for position " + position);
    }
}

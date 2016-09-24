package com.ataulm.chunks;

import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.novoda.viewpageradapter.ViewPagerAdapter;

final class ChunksPagerAdapter extends ViewPagerAdapter<RecyclerView> {

    private final ChunkEntryUserInteractions userInteractions;
    private final Resources resources;
    private Chunks chunks;

    ChunksPagerAdapter(ChunkEntryUserInteractions userInteractions, Resources resources, Chunks chunks) {
        this.userInteractions = userInteractions;
        this.resources = resources;
        this.chunks = chunks;
    }

    public void update(Chunks chunks) {
        this.chunks = chunks;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 2;
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
        Day day = getDayFor(position);
        Chunk chunk = getChunkFor(day);

        RecyclerView.Adapter adapter = view.getAdapter();
        if (adapter == null) {
            view.setAdapter(new ChunkRecyclerViewAdapter(userInteractions, day, chunk));
        } else {
            ((ChunkRecyclerViewAdapter) adapter).update(day, chunk);
            view.scrollToPosition(chunk.size() - 1);
        }
    }

    private Chunk getChunkFor(Day day) {
        switch (day) {
            case TODAY:
                return chunks.today();
            case TOMORROW:
                return chunks.tomorrow();
            default:
                throw new IllegalArgumentException("day not supported: " + day);
        }
    }

    private Day getDayFor(int position) {
        return DayToPagePositionMapper.getDayFor(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Day day = getDayFor(position);
        switch (day) {
            case TODAY:
                return resources.getString(R.string.days_tabs_today);
            case TOMORROW:
                return resources.getString(R.string.days_tabs_tomorrow);
            default:
                throw new IllegalArgumentException("no idea what to do for position " + position);
        }
    }

}

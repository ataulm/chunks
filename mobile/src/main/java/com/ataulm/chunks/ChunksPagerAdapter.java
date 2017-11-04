package com.ataulm.chunks;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.novoda.viewpageradapter.ViewPagerAdapter;

final class ChunksPagerAdapter extends ViewPagerAdapter<ChunksPage> {

    private final ItemUserInteractions userInteractions;
    private final OnPageChangeListenerDelegate onPageChangeListenerDelegate;
    private final Resources resources;

    private Chunks chunks;

    ChunksPagerAdapter(ItemUserInteractions userInteractions, OnPageChangeListenerDelegate onPageChangeListenerDelegate, Resources resources, Chunks chunks) {
        this.userInteractions = userInteractions;
        this.resources = resources;
        this.onPageChangeListenerDelegate = onPageChangeListenerDelegate;
        this.chunks = chunks;
    }

    public void update(Chunks chunks) {
        this.chunks = chunks;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return Day.values().length;
    }

    @Override
    protected ChunksPage createView(ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        ChunksPage chunksPage = (ChunksPage) layoutInflater.inflate(R.layout.view_entries_page, container, false);
        onPageChangeListenerDelegate.register(chunksPage);
        return chunksPage;
    }

    @Override
    protected void bindView(ChunksPage view, int position) {
        Day day = getDayFor(position);
        Items items = getChunkFor(day);

        view.update(items, userInteractions, day);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object key) {
        super.destroyItem(container, position, key);
        ChunksPage view = ((ChunksPage) key);
        onPageChangeListenerDelegate.deregister(view);
    }

    private Items getChunkFor(Day day) {
        switch (day) {
            case TODAY:
                return chunks.today();
            case TOMORROW:
                return chunks.tomorrow();
            case SOMETIME:
                return chunks.sometime();
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
            case SOMETIME:
                return resources.getString(R.string.days_tabs_sometime);
            default:
                throw new IllegalArgumentException("no idea what to do for position " + position);
        }
    }

}

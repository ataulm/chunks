package com.ataulm.chunks;

import android.support.v4.view.ViewPager;

class AndroidChunksView implements ChunksView {

    private final ViewPager viewPager;
    private final EntryView.UserInteractions entryViewUserInteractions;

    AndroidChunksView(ViewPager viewPager, EntryView.UserInteractions entryViewUserInteractions) {
        this.viewPager = viewPager;
        this.entryViewUserInteractions = entryViewUserInteractions;
    }

    @Override
    public void display(Chunks chunks) {
        updateViewPagerWith(chunks);
    }

    private void updateViewPagerWith(Chunks chunks) {
        ChunksPagerAdapter chunksPagerAdapter;
        if (viewPager.getAdapter() == null) {
            chunksPagerAdapter = new ChunksPagerAdapter(entryViewUserInteractions, chunks);
            viewPager.setAdapter(chunksPagerAdapter);
        } else {
            chunksPagerAdapter = (ChunksPagerAdapter) viewPager.getAdapter();
            chunksPagerAdapter.update(chunks);
        }
    }
/*
    private void foo() {
        entryInputView.setEnabled(true);

        dayTabsWidget.bind(
                new DayTabsWidget.Listener() {

                    @Override
                    public void onClick(Day day) {
                        int page = DayToPagePositionMapper.getPageFor(day);
                        viewPager.setCurrentItem(page);
                    }

                }
        );

        viewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {

                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        Day day = getDayFor(position);
                        dayTabsWidget.onDisplay(day);
                    }

                }
        );
    }
*/
}

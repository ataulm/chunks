package com.ataulm.chunks;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChunksScreen extends LinearLayout implements ChunksView {

    @BindView(R.id.chunks_screen_view_pager)
    ViewPager viewPager;

    @BindView(R.id.chunks_screen_pager_navigation_widget)
    TabsPagerNavigationWidget tabsPagerNavigationWidget;

    @BindView(R.id.chunks_screen_empty_input_widget)
    EntryInputWidget entryInputWidget;

    public ChunksScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_chunks_screen, this);
        ButterKnife.bind(this);

        setPagerNavigation(Day.TODAY);
        tabsPagerNavigationWidget.bind(
                new TabsPagerNavigationWidget.Listener() {

                    @Override
                    public void onClick(Day day) {
                        setPagerNavigation(day);
                        setViewPager(day);
                    }

                }
        );
    }

    private void setPagerNavigation(Day day) {
        tabsPagerNavigationWidget.display(day);
    }

    private void setViewPager(Day day) {
        int page = DayToPagePositionMapper.getPageFor(day);
        viewPager.setCurrentItem(page);
    }

    @Override
    public void display(Chunks chunks, ChunkEntryUserInteractions chunkEntryUserInteractions, final EntryInputUserInteractions entryInputUserInteractions) {
        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if (positionOffset != 0) {
                            // for now, only want stuff changing when the page has settled
                            return;
                        }

                        Day day = DayToPagePositionMapper.getDayFor(position);
                        setPagerNavigation(day);
                        entryInputWidget.bind(entryInputUserInteractions, day);
                    }
                }
        );

        updateViewPagerWith(chunks, chunkEntryUserInteractions);
    }

    private void updateViewPagerWith(Chunks chunks, ChunkEntryUserInteractions chunkEntryUserInteractions) {
        ChunksPagerAdapter chunksPagerAdapter;
        if (viewPager.getAdapter() == null) {
            chunksPagerAdapter = new ChunksPagerAdapter(chunkEntryUserInteractions, viewPager.getResources(), chunks);
            viewPager.setAdapter(chunksPagerAdapter);
            setViewPager(Day.TODAY); // TODO: this happens on rotate, but not necessarily what we want
        } else {
            chunksPagerAdapter = (ChunksPagerAdapter) viewPager.getAdapter();
            chunksPagerAdapter.update(chunks);
        }
    }

}
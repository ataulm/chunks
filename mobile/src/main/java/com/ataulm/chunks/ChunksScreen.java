package com.ataulm.chunks;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChunksScreen extends FrameLayout implements ChunksView {

    @BindView(R.id.chunks_screen_pager_navigation_widget)
    TabsPagerNavigationWidget tabsPagerNavigationWidget;

    @BindView(R.id.chunks_screen_view_pager)
    ViewPager viewPager;

    @BindView(R.id.chunks_screen_empty_input_widget)
    ItemInputWidget itemInputWidget;

    private final OnPageChangeListenerDelegate onPageChangeListenerDelegate = new OnPageChangeListenerDelegate();

    public ChunksScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_chunks_screen, this);
        ButterKnife.bind(this);
    }

    @Override
    public void display(Chunks chunks, ItemUserInteractions itemUserInteractions, final ItemInputUserInteractions itemInputUserInteractions) {
        if (chunks.input().isPresent()) {
            itemInputWidget.setText(chunks.input().get());
        }

        viewPager.clearOnPageChangeListeners();
        viewPager.addOnPageChangeListener(onPageChangeListenerDelegate);
        viewPager.addOnPageChangeListener(
                new ViewPager.SimpleOnPageChangeListener() {
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        if (positionOffset != 0) {
                            // for now, only want stuff changing when the page has settled
                            return;
                        }

                        Day day = DayToPagePositionMapper.getDayFor(position);
                        itemInputWidget.update(itemInputUserInteractions, day);
                    }
                }
        );

        updateViewPagerWith(chunks, itemUserInteractions);
    }

    private void updateViewPagerWith(Chunks chunks, ItemUserInteractions itemUserInteractions) {
        ChunksPagerAdapter chunksPagerAdapter;
        if (viewPager.getAdapter() == null) {
            chunksPagerAdapter = new ChunksPagerAdapter(itemUserInteractions, onPageChangeListenerDelegate, viewPager.getResources(), chunks);
            viewPager.setAdapter(chunksPagerAdapter);
            setViewPager(Day.TODAY); // TODO: this happens on rotate, but not necessarily what we want
        } else {
            chunksPagerAdapter = (ChunksPagerAdapter) viewPager.getAdapter();
            chunksPagerAdapter.update(chunks);
        }
        tabsPagerNavigationWidget.bind(viewPager);
    }

    private void setViewPager(Day day) {
        int page = DayToPagePositionMapper.getPageFor(day);
        viewPager.setCurrentItem(page);
    }

}

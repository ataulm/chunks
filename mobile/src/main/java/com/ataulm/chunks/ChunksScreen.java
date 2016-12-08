package com.ataulm.chunks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.ataulm.chunks.settings.SettingsActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChunksScreen extends LinearLayout implements ChunksView {

    @BindView(R.id.chunks_screen_toolbar)
    Toolbar toolbar;

    @BindView(R.id.chunks_screen_pager_navigation_widget)
    TabsPagerNavigationWidget tabsPagerNavigationWidget;

    @BindView(R.id.chunks_screen_view_pager)
    ViewPager viewPager;

    @BindView(R.id.chunks_screen_empty_input_widget)
    EntryInputWidget entryInputWidget;

    private final OnPageChangeListenerDelegate onPageChangeListenerDelegate = new OnPageChangeListenerDelegate();

    public ChunksScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(VERTICAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_chunks_screen, this);
        ButterKnife.bind(this);

        toolbar.setTitle(R.string.chunks_app_bar_title);
        toolbar.setTitleTextColor(Color.WHITE);

        if (new SettingsFeature().isEnabled()) {
            inflateToolbarMenu();
        }
    }

    private void inflateToolbarMenu() {
        toolbar.inflateMenu(R.menu.menu_chunks);
        toolbar.setOnMenuItemClickListener(
                new Toolbar.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        if (item.getItemId() == R.id.menu_chunks_settings) {
                            Intent intent = new Intent(getContext(), SettingsActivity.class);
                            getContext().startActivity(intent);
                            return true;
                        }
                        return false;
                    }
                }
        );

    }

    @Override
    public void display(Chunks chunks, ChunkEntryUserInteractions chunkEntryUserInteractions, final EntryInputUserInteractions entryInputUserInteractions) {
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
                        entryInputWidget.bind(entryInputUserInteractions, day);
                    }
                }
        );

        updateViewPagerWith(chunks, chunkEntryUserInteractions);
    }

    private void updateViewPagerWith(Chunks chunks, ChunkEntryUserInteractions chunkEntryUserInteractions) {
        ChunksPagerAdapter chunksPagerAdapter;
        if (viewPager.getAdapter() == null) {
            chunksPagerAdapter = new ChunksPagerAdapter(chunkEntryUserInteractions, onPageChangeListenerDelegate, viewPager.getResources(), chunks);
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

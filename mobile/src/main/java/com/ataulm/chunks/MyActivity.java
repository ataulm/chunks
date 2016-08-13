package com.ataulm.chunks;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.ataulm.AndroidLog;
import com.ataulm.Event;
import com.ataulm.LoggingObserver;
import com.ataulm.Optional;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.ataulm.chunks.DayToPagePositionMapper.getDayFor;
import static com.ataulm.chunks.DayToPagePositionMapper.getPageFor;

public class MyActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    DayTabsWidget dayTabsWidget;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.entry_input_view)
    EntryInputView entryInputView;

    private ChunksService chunksService;
    private Subscription subscription;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my);
        ButterKnife.bind(this);

        chunksService = ((ChunksApplication) getApplication()).getChunksService();
    }

    @Override
    protected void onResume() {
        super.onResume();
        entryInputView.bind(
                new EntryInputView.Listener() {

                    @Override
                    public void onClickAddEntry(String value) {
                        int currentItem = viewPager.getCurrentItem();
                        Entry entry = Entry.createNew(value, getDayFor(currentItem));
                        entryInputView.setEnabled(false);
                        chunksService.createEntry(entry);
                    }

                }
        );

        subscription = chunksService.fetchEntries()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new LoggingObserver<Event<Chunks>>(new AndroidLog()) {

                            @Override
                            public void onNext(Event<Chunks> event) {
                                // TODO: handle loading/errors (checkable from event)
                                Optional<Chunks> data = event.getData();
                                if (data.isPresent()) {
                                    updateViews(data.get());
                                } else {
                                    // TODO: show empty screen
                                }
                            }

                        }
                );
    }

    private void updateViews(Chunks chunks) {
        entryInputView.setEnabled(true);

        ChunksPagerAdapter chunksPagerAdapter;
        if (viewPager.getAdapter() == null) {
            chunksPagerAdapter = ChunksPagerAdapter.newInstance(chunks);
            viewPager.setAdapter(chunksPagerAdapter);
        } else {
            chunksPagerAdapter = (ChunksPagerAdapter) viewPager.getAdapter();
            chunksPagerAdapter.update(chunks);
        }

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
        viewPager.setCurrentItem(getPageFor(Day.TODAY));
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!subscription.isUnsubscribed()) {
            subscription.unsubscribe();
        }
        chunksService.persist();
    }

}

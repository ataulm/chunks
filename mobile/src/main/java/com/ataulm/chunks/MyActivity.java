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
import rx.subscriptions.CompositeSubscription;

public class MyActivity extends AppCompatActivity {

    @BindView(R.id.tabs)
    DayTabsWidget dayTabsWidget;

    @BindView(R.id.view_pager)
    ViewPager viewPager;

    @BindView(R.id.entry_input_view)
    EntryInputView entryInputView;

    private ChunksService chunksService;
    private CompositeSubscription subscriptions;

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
        subscriptions = new CompositeSubscription();

        entryInputView.bind(new EntryInputView.Listener() {
            @Override
            public void onClickAddEntry(String value) {
                Entry entry = Entry.createNew(value, getVisibleDay());
                entryInputView.setEnabled(false);
                chunksService.createEntry(entry);
            }

            private Day getVisibleDay() {
                int currentItem = viewPager.getCurrentItem();
                switch (currentItem) {
                    case 0:
                        return Day.YESTERDAY;
                    case 1:
                        return Day.TODAY;
                    case 2:
                        return Day.TOMORROW;
                    default:
                        throw new IllegalStateException("unexpected page: " + currentItem);
                }
            }
        });

        Subscription entriesSubscription = chunksService.fetchEntries()
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

        subscriptions.add(entriesSubscription);
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
        dayTabsWidget.bind(new DayTabsWidget.Listener() {
            @Override
            public void onClick(Day day) {
                switch (day) {
                    case YESTERDAY:
                        viewPager.setCurrentItem(0);
                        break;
                    case TODAY:
                        viewPager.setCurrentItem(1);
                        break;
                    case TOMORROW:
                        viewPager.setCurrentItem(2);
                        break;
                    default:
                        throw new IllegalStateException("unexpected day: " + day);
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        subscriptions.clear();
        chunksService.persist();
    }

}

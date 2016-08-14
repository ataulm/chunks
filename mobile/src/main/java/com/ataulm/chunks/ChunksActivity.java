package com.ataulm.chunks;

import android.os.Bundle;
import android.support.v4.view.ViewPager;

import butterknife.ButterKnife;

public class ChunksActivity extends BaseActivity {

    private ChunksPresenter chunksPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chunks);
        ButterKnife.bind(this);

        ChunksService chunksService = ((ChunksApplication) getApplication()).getChunksService();

        ViewPager viewPager = ButterKnife.findById(this, R.id.view_pager);
        DayTabsWidget dayTabsWidget = ButterKnife.findById(this, R.id.tabs);
        EntryInputView entryInputView = ButterKnife.findById(this, R.id.entry_input_view);

        EntryView.UserInteractions entryViewUserInteractions = new AndroidEntryViewUserInteractions(chunksService);
        ChunksView chunksView = new AndroidChunksView(viewPager, entryViewUserInteractions);

        chunksPresenter = new AndroidChunksPresenter(chunksService, chunksView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        chunksPresenter.startPresenting();
    }

    @Override
    protected void onPause() {
        chunksPresenter.stopPresenting();
        super.onPause();
    }

}

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

        ChunksService chunksService = ((ChunksApplication) getApplication()).getChunksService();
        ChunksView chunksView = ButterKnife.findById(this, R.id.chunks_screen);
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

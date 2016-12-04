package com.ataulm.chunks;

import android.content.Intent;
import android.os.Bundle;

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

        Intent intent = getIntent();
        if (intent.hasExtra(Intent.EXTRA_TEXT)) {
            String text = intent.getStringExtra(Intent.EXTRA_TEXT);
            intent.removeExtra(Intent.EXTRA_TEXT);
            chunksPresenter.onExternalShareText(text);
        }
    }

    @Override
    protected void onPause() {
        chunksPresenter.stopPresenting();
        super.onPause();
    }

}

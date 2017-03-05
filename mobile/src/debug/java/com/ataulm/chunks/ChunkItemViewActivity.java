package com.ataulm.chunks;

import android.app.Activity;
import android.os.Bundle;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChunkItemViewActivity extends Activity {

    @BindView(R.id.test_view)
    ChunkItemView chunkItemView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity_chunk_item_view);
        ButterKnife.bind(this);
    }

}

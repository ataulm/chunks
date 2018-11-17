package com.ataulm.chunks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ChunksPage extends FrameLayout {

    @BindView(R.id.chunks_page_recycler_view)
    ChunkRecyclerView recyclerView;

    @BindView(R.id.chunks_page_text_empty)
    TextView emptyTextView;

    public ChunksPage(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_chunks_page, this);
        ButterKnife.bind(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    public void update(Items items, ItemUserInteractions userInteractions, Day day) {
        if (items.isEmpty()) {
            emptyTextView.setVisibility(VISIBLE);
        } else {
            emptyTextView.setVisibility(GONE);
        }

        recyclerView.update(items, userInteractions, day);
    }

}

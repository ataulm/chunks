package com.ataulm.chunks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryView extends FrameLayout {

    @BindView(R.id.entry_text_view)
    TextView entryTextView;

    public EntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_entry, this);
        ButterKnife.bind(this);
    }

    public void bind(Entry entry) {
        entryTextView.setText(entry.value());
    }

}

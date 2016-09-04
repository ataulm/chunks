package com.ataulm.chunks;

import android.content.Context;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChunksToolbar extends Toolbar {

    @BindView(R.id.chunks_toolbar_text_title)
    TextView titleTextView;

    public ChunksToolbar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_chunks_toolbar, this);
        ButterKnife.bind(this);
    }

    @Override
    public void setTitle(@StringRes int resId) {
        titleTextView.setText(resId);
    }

    @Override
    public void setTitle(CharSequence title) {
        titleTextView.setText(title);
    }

}

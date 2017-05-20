package com.ataulm.chunks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChunkItemView extends LinearLayout {

    @BindView(R.id.entry_check_box)
    CheckBox checkBox;

    @BindView(R.id.entry_text_view)
    TextView entryTextView;

    @BindView(R.id.entry_button_move_left)
    View moveLeftButton;

    @BindView(R.id.entry_button_move_right)
    View moveRightButton;

    @BindView(R.id.entry_button_menu)
    View menuButton;

    public ChunkItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_chunk_item, this);
        ButterKnife.bind(this);
    }

    public CheckBox checkBox() {
        return checkBox;
    }

    public TextView entryTextView() {
        return entryTextView;
    }

    public View moveLeftButton() {
        return moveLeftButton;
    }

    public View moveRightButton() {
        return moveRightButton;
    }

    public View menuButton() {
        return menuButton;
    }

}

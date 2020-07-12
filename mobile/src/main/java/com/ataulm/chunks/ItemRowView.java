package com.ataulm.chunks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemRowView extends LinearLayout {

    @BindView(R.id.item_check_box)
    CheckBox checkBox;

    @BindView(R.id.item_text_view)
    TextView itemTextView;

    @BindView(R.id.item_button_move_left)
    View moveLeftButton;

    @BindView(R.id.item_button_move_right)
    View moveRightButton;

    public ItemRowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
        super.setBaselineAligned(false);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_item, this);
        ButterKnife.bind(this);
    }

    public CheckBox checkBox() {
        return checkBox;
    }

    public TextView itemTextView() {
        return itemTextView;
    }

    public View moveLeftButton() {
        return moveLeftButton;
    }

    public View moveRightButton() {
        return moveRightButton;
    }

}

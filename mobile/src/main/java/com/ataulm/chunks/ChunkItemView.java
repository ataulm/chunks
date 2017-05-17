package com.ataulm.chunks;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.novoda.accessibility.AccessibilityServices;
import com.novoda.accessibility.ActionsAlertDialogCreator;

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

    private final AccessibilityServices accessibilityServices;
    private final ActionsAlertDialogCreator actionsAlertDialogCreator;

    public ChunkItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
        accessibilityServices = AccessibilityServices.newInstance(context);
        actionsAlertDialogCreator = new ActionsAlertDialogCreator(context);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_chunk_item, this);
        ButterKnife.bind(this);
    }

    // TODO: perhaps we don't get ChunkEntryUserInteractions here - rather, we get ChunkActions which has optional actions. That way this view can be dumber and cannot do something it isn't eligible to do (causing an error).
    public void bind(Chunk chunk, Day day, final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        final ChunksActions chunksActions = ChunksActions.create(chunk, day, entry, userInteractions);
        final AlertDialog alertDialog = actionsAlertDialogCreator.create(chunksActions.actions());

        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(entry.isCompleted());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                toggleCompleted(entry, chunksActions);
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCompleted(entry, chunksActions);
            }
        });

        entryTextView.setText(entry.value());

        if (day == Day.TODAY) {
            moveLeftButton.setVisibility(GONE);
            moveRightButton.setVisibility(VISIBLE);
            moveRightButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    chunksActions.moveToTomorrowAction().run();
                }
            });
        } else if (day == Day.TOMORROW) {
            moveLeftButton.setVisibility(VISIBLE);
            moveLeftButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    chunksActions.moveToTodayAction().run();
                }
            });

            moveRightButton.setVisibility(GONE);
        } else {
            moveLeftButton.setVisibility(VISIBLE);
            moveLeftButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    chunksActions.moveToTomorrowAction().run();
                }
            });

            moveRightButton.setVisibility(GONE);
        }

        menuButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        if (accessibilityServices.isSpokenFeedbackEnabled()) {
            new ItemViewClickActions(this).setClickListeners(chunksActions.actions());
            moveLeftButton.setVisibility(GONE);
            moveRightButton.setVisibility(GONE);
            menuButton.setVisibility(GONE);
        }
    }

    private void toggleCompleted(Entry entry, ChunksActions chunksActions) {
        if (entry.isCompleted()) {
            chunksActions.markNotCompleteAction().run();
        } else {
            chunksActions.markCompleteAction().run();
        }
    }

}

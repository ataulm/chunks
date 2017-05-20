package com.ataulm.chunks;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ataulm.Optional;
import com.novoda.accessibility.AccessibilityServices;
import com.novoda.accessibility.Action;
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

    public void bind(Day day, Entry entry, final ChunksActions chunksActions) {
        final AlertDialog alertDialog = actionsAlertDialogCreator.create(chunksActions.actions());

        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(entry.isCompleted());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                toggleCompleted(chunksActions);
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCompleted(chunksActions);
            }
        });

        entryTextView.setText(entry.value());

        final Optional<Action> transitionNextDay = chunksActions.transitionToNextDay();
        if (transitionNextDay.isPresent()) {
            moveLeftButton.setVisibility(GONE);
            moveRightButton.setVisibility(VISIBLE);
            moveRightButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    transitionNextDay.get().run();
                }
            });
        }

        final Optional<Action> transitionPreviousDay = chunksActions.transitionToPreviousDay();
        if (transitionPreviousDay.isPresent()) {
            moveRightButton.setVisibility(GONE);
            moveLeftButton.setVisibility(VISIBLE);
            moveLeftButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    transitionPreviousDay.get().run();
                }
            });
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

    private void toggleCompleted(ChunksActions chunksActions) {
        if (chunksActions.markComplete().isPresent()) {
            chunksActions.markComplete().get().run();
        } else if (chunksActions.markNotComplete().isPresent()) {
            chunksActions.markNotComplete().get().run();
        } else {
            throw new IllegalStateException("actions has neither complete/incomplete? " + chunksActions);
        }
    }

}

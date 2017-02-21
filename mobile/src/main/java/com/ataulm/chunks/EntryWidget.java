package com.ataulm.chunks;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.novoda.accessibility.ActionsAlertDialogCreator;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryWidget extends LinearLayout {

    @BindView(R.id.entry_check_box)
    CheckBox checkBox;

    @BindView(R.id.entry_text_view)
    TextView entryTextView;

    @BindView(R.id.entry_button_move_left)
    TextView moveLeftButton;

    @BindView(R.id.entry_button_move_right)
    TextView moveRightButton;

    @BindView(R.id.entry_button_menu)
    TextView menuButton;
    private ActionsAlertDialogCreator actionsAlertDialogCreator;

    public EntryWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_entry, this);
        ButterKnife.bind(this);
        actionsAlertDialogCreator = new ActionsAlertDialogCreator(getContext());
    }

    public void bind(Day day, final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        final ChunksActions chunksActions = ChunksActions.create(day, entry, userInteractions);
        final AlertDialog alertDialog = actionsAlertDialogCreator.create(chunksActions.actions());

        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(entry.isCompleted());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                toggleCompleted(entry, userInteractions);
            }
        });

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCompleted(entry, userInteractions);
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
    }


    private void toggleCompleted(Entry entry, ChunkEntryUserInteractions userInteractions) {
        if (entry.isCompleted()) {
            userInteractions.onUserMarkNotComplete(entry);
        } else {
            userInteractions.onUserMarkComplete(entry);
        }
    }

}

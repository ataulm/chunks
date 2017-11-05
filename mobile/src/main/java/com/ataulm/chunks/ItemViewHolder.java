package com.ataulm.chunks;

import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.ataulm.Optional;
import com.novoda.accessibility.AccessibilityServices;
import com.novoda.accessibility.Action;
import com.novoda.accessibility.ActionsAlertDialogCreator;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

final class ItemViewHolder extends RecyclerView.ViewHolder {

    private final ItemView itemView;
    private final ItemViewClickActions itemViewClickActions;
    private final AccessibilityServices accessibilityServices;
    private final ActionsAlertDialogCreator actionsAlertDialogCreator;

    public static ItemViewHolder inflate(ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        ItemView view = (ItemView) layoutInflater.inflate(R.layout.view_chunk_item, parent, false);
        return new ItemViewHolder(view);
    }

    ItemViewHolder(ItemView itemView) {
        super(itemView);
        this.itemView = itemView;

        this.itemViewClickActions = new ItemViewClickActions(itemView);
        this.accessibilityServices = AccessibilityServices.newInstance(itemView.getContext());
        this.actionsAlertDialogCreator = new ActionsAlertDialogCreator(itemView.getContext());
    }

    public void bind(Item item, final ChunksActions chunksActions, final DragStartListener dragStartListener) {
        final AlertDialog alertDialog = actionsAlertDialogCreator.create(chunksActions.actions());

        itemView.dragHandle().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    dragStartListener.onStartDrag(ItemViewHolder.this);
                }
                return false;
            }
        });

        itemView.checkBox().setOnCheckedChangeListener(null);
        itemView.checkBox().setChecked(item.isCompleted());
        itemView.checkBox().setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                toggleCompleted(chunksActions);
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleCompleted(chunksActions);
            }
        });

        itemView.itemTextView().setText(item.value());

        final Optional<Action> transitionNextDay = chunksActions.transitionToNextDay();
        if (transitionNextDay.isPresent()) {
            itemView.moveLeftButton().setVisibility(GONE);
            itemView.moveRightButton().setVisibility(VISIBLE);
            itemView.moveRightButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transitionNextDay.get().run();
                }
            });
        }

        final Optional<Action> transitionPreviousDay = chunksActions.transitionToPreviousDay();
        if (transitionPreviousDay.isPresent()) {
            itemView.moveRightButton().setVisibility(GONE);
            itemView.moveLeftButton().setVisibility(VISIBLE);
            itemView.moveLeftButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    transitionPreviousDay.get().run();
                }
            });
        }

        itemView.menuButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.show();
            }
        });

        if (accessibilityServices.isSpokenFeedbackEnabled()) {
            itemViewClickActions.setClickListeners(chunksActions.actions());
            itemView.moveLeftButton().setVisibility(GONE);
            itemView.moveRightButton().setVisibility(GONE);
            itemView.menuButton().setVisibility(GONE);
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

    interface DragStartListener {

        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }
}

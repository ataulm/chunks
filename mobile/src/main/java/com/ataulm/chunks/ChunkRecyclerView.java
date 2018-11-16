package com.ataulm.chunks;

import android.content.Context;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.util.AttributeSet;

import com.novoda.accessibility.AccessibilityServices;

public class ChunkRecyclerView extends RecyclerView {

    private ItemTouchHelper itemTouchHelper;
    private ItemViewHolder.DragStartListener dragStartListener;

    public ChunkRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        boolean spokenFeedbackEnabled = AccessibilityServices.newInstance(getContext()).isSpokenFeedbackEnabled();
        if (!spokenFeedbackEnabled) {
            itemTouchHelper = attachDragAndDropItemTouchHelper();
            itemTouchHelper.attachToRecyclerView(this);

            dragStartListener = new ItemViewHolder.DragStartListener() {
                @Override
                public void onStartDrag(ViewHolder viewHolder) {
                    itemTouchHelper.startDrag(viewHolder);
                }
            };
        }
    }

    private ItemTouchHelper attachDragAndDropItemTouchHelper() {
        ItemDragCallback.ItemMoveCallback itemMoveCallback = new ItemDragCallback.ItemMoveCallback() {
            @Override
            public void onItemMoving(int startPosition, int endPosition) {
                getAdapter().onItemMoving(startPosition, endPosition);
            }

            @Override
            public void onItemMoved(int endPosition) {
                getAdapter().onItemMoved(endPosition);
            }
        };
        return new ItemTouchHelper(new ItemDragCallback(itemMoveCallback));
    }

    public void update(Items items, ItemUserInteractions userInteractions, Day day) {
        ChunkRecyclerViewAdapter adapter = getAdapter();
        if (adapter == null) {
            adapter = new ChunkRecyclerViewAdapter(userInteractions, dragStartListener, day, items);
            setAdapter(adapter);
        } else {
            boolean itemAdded = adapter.update(day, items);
            if (itemAdded) {
                scrollToPosition(adapter.getItemCount() - 1);
            }
        }
    }

    @Override
    public final ChunkRecyclerViewAdapter getAdapter() {
        return (ChunkRecyclerViewAdapter) super.getAdapter();
    }

    @Override
    public final void setAdapter(Adapter adapter) {
        if (adapter instanceof ChunkRecyclerViewAdapter) {
            super.setAdapter(adapter);
        }
    }

    private static class ItemDragCallback extends ItemTouchHelper.Callback {

        private final ItemMoveCallback itemMoveCallback;

        ItemDragCallback(ItemMoveCallback itemMoveCallback) {
            this.itemMoveCallback = itemMoveCallback;
        }

        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder source, RecyclerView.ViewHolder target) {
            int sourcePosition = source.getAdapterPosition();
            int targetPosition = target.getAdapterPosition();
            itemMoveCallback.onItemMoving(sourcePosition, targetPosition);
            return true;
        }

        @Override
        public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);
            itemMoveCallback.onItemMoved(viewHolder.getAdapterPosition());
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int i) {
        }

        interface ItemMoveCallback {

            void onItemMoving(int startPosition, int endPosition);

            void onItemMoved(int endPosition);

        }

    }

}

package com.ataulm.chunks;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.util.AttributeSet;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryWidget extends LinearLayout {

    @BindView(R.id.entry_text_view)
    TextView entryTextView;

    @BindView(R.id.entry_button_see_all_actions)
    TextView seeAllActionsButton;

    public EntryWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_entry, this);
        ButterKnife.bind(this);
    }

    public void bind(Entry entry, ChunkEntryUserInteractions userInteractions) {
        entryTextView.setText(entry.value());

        final PopupMenu popupMenu = setupPopupMenuListener(entry, userInteractions);
        seeAllActionsButton.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popupMenu.show();
                    }

                }
        );
    }

    private PopupMenu setupPopupMenuListener(Entry entry, ChunkEntryUserInteractions userInteractions) {
        PopupMenu popupMenu = new PopupMenu(getContext(), seeAllActionsButton);
        popupMenu.inflate(R.menu.menu_entry);
        popupMenu.setOnMenuItemClickListener(new MenuClickListener(entry, userInteractions));
        return popupMenu;
    }

    private static class MenuClickListener implements PopupMenu.OnMenuItemClickListener {

        private final Entry entry;
        private final ChunkEntryUserInteractions userInteractions;


        MenuClickListener(Entry entry, ChunkEntryUserInteractions userInteractions) {
            this.entry = entry;
            this.userInteractions = userInteractions;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.mark_complete:
                    userInteractions.onUserMarkComplete(entry);
                    return true;
                case R.id.mark_not_complete:
                    userInteractions.onUserMarkNotComplete(entry);
                    return true;
                case R.id.move_to_today:
                    userInteractions.onUserTransitionEntry(entry, Day.TODAY);
                    return true;
                case R.id.move_to_tomorrow:
                    userInteractions.onUserTransitionEntry(entry, Day.TOMORROW);
                    return true;
                case R.id.remove:
                    userInteractions.onUserRemove(entry);
                    return true;
                default:
                    throw new IllegalArgumentException("unhandled item: " + item.getTitle());
            }
        }

    }

}

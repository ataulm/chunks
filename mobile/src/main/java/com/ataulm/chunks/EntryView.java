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

public class EntryView extends LinearLayout {

    @BindView(R.id.entry_text_view)
    TextView entryTextView;

    @BindView(R.id.entry_button_see_all_actions)
    TextView seeAllActionsButton;

    public EntryView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_entry, this);
        ButterKnife.bind(this);
    }

    public void bind(Entry entry, UserInteractions userInteractions) {
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

    interface UserInteractions {

        void onUserMarkComplete(Entry entry);

        void onUserMarkNotComplete(Entry entry);

        void onUserMoveToTomorrow(Entry entry);

        void onUserRemove(Entry entry);

    }

    private PopupMenu setupPopupMenuListener(Entry entry, UserInteractions userInteractions) {
        PopupMenu popupMenu = new PopupMenu(getContext(), seeAllActionsButton);
        popupMenu.inflate(R.menu.menu_entry);
        popupMenu.setOnMenuItemClickListener(new MenuClickListener(entry, userInteractions));
        return popupMenu;
    }

    private static class MenuClickListener implements PopupMenu.OnMenuItemClickListener {

        private final Entry entry;
        private final UserInteractions userInteractions;


        MenuClickListener(Entry entry, UserInteractions userInteractions) {
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
                    userInteractions.onUserMarkComplete(entry);
                    return true;
                case R.id.move_to_tomorrow:
                    userInteractions.onUserMoveToTomorrow(entry);
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

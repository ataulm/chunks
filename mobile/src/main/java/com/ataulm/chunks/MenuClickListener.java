package com.ataulm.chunks;

import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;

class MenuClickListener implements PopupMenu.OnMenuItemClickListener {

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

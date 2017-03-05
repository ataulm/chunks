package com.ataulm.chunks;

import android.support.annotation.IdRes;
import android.util.SparseBooleanArray;

class HitCounter implements ChunkEntryUserInteractions {

    private final SparseBooleanArray hits = new SparseBooleanArray();

    @Override
    public void onUserMarkComplete(Entry entry) {
        hits.put(R.id.hits_on_user_mark_complete, true);
    }

    @Override
    public void onUserMarkNotComplete(Entry entry) {
        hits.put(R.id.hits_on_user_mark_not_complete, true);
    }

    @Override
    public void onUserTransitionEntry(Entry entry, Day day) {
        if (day == Day.TODAY) {
            hits.put(R.id.hits_on_user_transition_entry_today, true);
        }

        if (day == Day.TOMORROW) {
            hits.put(R.id.hits_on_user_transition_entry_tomorrow, true);
        }

        if (day == Day.SOMETIME) {
            hits.put(R.id.hits_on_user_transition_entry_sometime, true);
        }
    }

    @Override
    public void onUserEdit(Entry entry) {
        hits.put(R.id.hits_on_user_edit_entry, true);
    }

    @Override
    public void onUserRemove(Entry entry) {
        hits.put(R.id.hits_on_user_remove_entry, true);
    }

    public void assertHit(@IdRes int id) {
        if (!hit(id)) {
            throw new AssertionError("Expected hit, but didn't get one");
        }
    }

    public void assertNoHit(@IdRes int id) {
        if (hit(id)) {
            throw new AssertionError("Expected no hit, but got one");
        }
    }

    private boolean hit(@IdRes int id) {
        return hits.get(id, false);
    }

}

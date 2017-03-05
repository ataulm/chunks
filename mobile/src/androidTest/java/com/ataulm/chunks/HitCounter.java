package com.ataulm.chunks;

import android.content.res.Resources;
import android.support.annotation.StringRes;

import java.util.HashMap;
import java.util.Map;

class HitCounter implements ChunkEntryUserInteractions {

    private final Map<CharSequence, Boolean> hits = new HashMap<>();
    private final Resources resources;

    HitCounter(Resources resources) {
        this.resources = resources;
    }

    @Override
    public void onUserMarkComplete(Entry entry) {
        markHit(R.string.hits_on_user_mark_complete);
    }

    @Override
    public void onUserMarkNotComplete(Entry entry) {
        markHit(R.string.hits_on_user_mark_not_complete);
    }

    @Override
    public void onUserTransitionEntry(Entry entry, Day day) {
        if (day == Day.TODAY) {
            markHit(R.string.hits_on_user_transition_entry_today);
        }

        if (day == Day.TOMORROW) {
            markHit(R.string.hits_on_user_transition_entry_tomorrow);
        }

        if (day == Day.SOMETIME) {
            markHit(R.string.hits_on_user_transition_entry_sometime);
        }
    }

    @Override
    public void onUserEdit(Entry entry) {
        markHit(R.string.hits_on_user_edit_entry);
    }

    @Override
    public void onUserRemove(Entry entry) {
        markHit(R.string.hits_on_user_remove_entry);
    }

    private void markHit(@StringRes int id) {
        hits.put(resources.getString(id), true);
    }

    public void assertHit(@StringRes int id) {
        CharSequence key = getKey(id);
        assertHit(key);
    }

    public void assertHit(CharSequence key) {
        if (!hit(key)) {
            throw new AssertionError("Wanted hit, but no hits found: " + key);
        }
    }

    public void assertNoHit(@StringRes int id) {
        CharSequence key = getKey(id);
        assertNoHit(key);
    }

    public void assertNoHit(CharSequence key) {
        if (hit(key)) {
            throw new AssertionError("Wanted no hit, but got one: " + key);
        }
    }

    private CharSequence getKey(@StringRes int id) {
        return resources.getString(id);
    }

    private boolean hit(CharSequence key) {
        if (hits.containsKey(key)) {
            return hits.get(key);
        }
        return false;
    }

}

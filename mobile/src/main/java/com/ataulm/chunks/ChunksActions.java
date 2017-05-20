package com.ataulm.chunks;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;
import com.novoda.accessibility.Action;
import com.novoda.accessibility.Actions;

import java.util.ArrayList;

@AutoValue
abstract class ChunksActions {

    public static ChunksActions create(Chunk chunk, Day day, Entry entry, ChunkEntryUserInteractions userInteractions) {
        Optional<Action> markComplete = createMarkCompleteAction(entry, userInteractions);
        Optional<Action> markNotComplete = createMarkNotCompleteAction(entry, userInteractions);
        Action edit = createEditAction(entry, userInteractions);
        Optional<Action> transitionToPreviousDay = createTransitionToPreviousDayAction(day, entry, userInteractions);
        Optional<Action> transitionToNextDay = createTransitionToNextDayAction(day, entry, userInteractions);
        Optional<Action> moveUp = createMoveUpAction(chunk, entry, userInteractions);
        Optional<Action> moveDown = createMoveDownAction(chunk, entry, userInteractions);
        Action delete = createDeleteAction(entry, userInteractions);

        Actions actions = collate(markComplete, markNotComplete, edit, transitionToPreviousDay, transitionToNextDay, moveUp, moveDown, delete);
        return new AutoValue_ChunksActions(actions, markComplete, markNotComplete, edit, transitionToPreviousDay, transitionToNextDay, moveUp, moveDown, delete);
    }

    private static Optional<Action> createMarkCompleteAction(final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        if (entry.isCompleted()) {
            return Optional.absent();
        }
        return Optional.of(new Action(R.id.action_mark_complete, R.string.action_mark_complete, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserMarkComplete(entry);
            }
        }));
    }

    private static Optional<Action> createMarkNotCompleteAction(final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        if (!entry.isCompleted()) {
            return Optional.absent();
        }
        return Optional.of(new Action(R.id.action_mark_complete, R.string.action_mark_complete, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserMarkComplete(entry);
            }
        }));
    }

    private static Action createEditAction(final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        return new Action(R.id.action_edit, R.string.action_edit, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserEdit(entry);
            }
        });
    }

    private static Optional<Action> createTransitionToPreviousDayAction(final Day day, final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        switch (day) {
            case TODAY:
                return Optional.absent();
            case TOMORROW:
                return Optional.of(new Action(R.id.action_move_to_today, R.string.action_move_to_today, new Runnable() {
                    @Override
                    public void run() {
                        userInteractions.onUserTransitionEntry(entry, Day.TODAY);
                    }
                }));
            case SOMETIME:
                return Optional.of(new Action(R.id.action_move_to_tomorrow, R.string.action_move_to_tomorrow, new Runnable() {
                    @Override
                    public void run() {
                        userInteractions.onUserTransitionEntry(entry, Day.TOMORROW);
                    }
                }));
            default:
                throw new IllegalArgumentException("unhandled day: " + day);
        }
    }

    private static Optional<Action> createTransitionToNextDayAction(final Day day, final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        switch (day) {
            case TODAY:
                return Optional.of(new Action(R.id.action_move_to_tomorrow, R.string.action_move_to_tomorrow, new Runnable() {
                    @Override
                    public void run() {
                        userInteractions.onUserTransitionEntry(entry, Day.TOMORROW);
                    }
                }));
            case TOMORROW:
                return Optional.of(new Action(R.id.action_move_to_later, R.string.action_move_to_later, new Runnable() {
                    @Override
                    public void run() {
                        userInteractions.onUserTransitionEntry(entry, Day.SOMETIME);
                    }
                }));
            case SOMETIME:
                return Optional.absent();
            default:
                throw new IllegalArgumentException("unhandled day: " + day);
        }
    }

    private static Optional<Action> createMoveUpAction(final Chunk chunk, final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        final int originalEntryPosition = chunk.entries().indexOf(entry);
        if (originalEntryPosition == 0) {
            return Optional.absent();
        }
        return Optional.of(new Action(R.id.action_move_up, R.string.action_move_up, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserMove(entry, originalEntryPosition - 1);
            }
        }));
    }

    private static Optional<Action> createMoveDownAction(final Chunk chunk, final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        final int originalEntryPosition = chunk.entries().indexOf(entry);
        if (originalEntryPosition == chunk.size() - 1) {
            return Optional.absent();
        }
        return Optional.of(new Action(R.id.action_move_down, R.string.action_move_down, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserMove(entry, originalEntryPosition + 1);
            }
        }));
    }

    private static Action createDeleteAction(final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        return new Action(R.id.action_delete, R.string.action_delete, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserRemove(entry);
            }
        });
    }

    private static Actions collate(
            Optional<Action> markComplete,
            Optional<Action> markNotComplete,
            Action edit,
            Optional<Action> transitionToPreviousDay,
            Optional<Action> transitionToNextDay,
            Optional<Action> moveUp,
            Optional<Action> moveDown,
            Action delete
    ) {
        ArrayList<Action> actions = new ArrayList<>();
        if (markComplete.isPresent()) {
            actions.add(markComplete.get());
        }
        if (markNotComplete.isPresent()) {
            actions.add(markNotComplete.get());
        }
        actions.add(edit);
        if (transitionToPreviousDay.isPresent()) {
            actions.add(transitionToPreviousDay.get());
        }
        if (transitionToNextDay.isPresent()) {
            actions.add(transitionToNextDay.get());
        }
        if (moveUp.isPresent()) {
            actions.add(moveUp.get());
        }
        if (moveDown.isPresent()) {
            actions.add(moveDown.get());
        }
        actions.add(delete);
        return new Actions(actions);
    }

    abstract Actions actions();

    abstract Optional<Action> markComplete();

    abstract Optional<Action> markNotComplete();

    abstract Action edit();

    abstract Optional<Action> transitionToPreviousDay();

    abstract Optional<Action> transitionToNextDay();

    abstract Optional<Action> moveUp();

    abstract Optional<Action> moveDown();

    abstract Action delete();

}

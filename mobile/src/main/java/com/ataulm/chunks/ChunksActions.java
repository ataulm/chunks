package com.ataulm.chunks;

import com.ataulm.Optional;
import com.google.auto.value.AutoValue;
import com.novoda.accessibility.Action;
import com.novoda.accessibility.Actions;

import java.util.ArrayList;

@AutoValue
abstract class
ChunksActions {

    public static ChunksActions create(Items items, Day day, Item item, ItemUserInteractions userInteractions) {
        Optional<Action> markComplete = createMarkCompleteAction(item, userInteractions);
        Optional<Action> markNotComplete = createMarkNotCompleteAction(item, userInteractions);
        Action edit = createEditAction(item, userInteractions);
        Optional<Action> transitionToToday = createTransitionToTodayAction(day, item, userInteractions);
        Optional<Action> transitionToTomorrow = createTransitionToTomorrowAction(day, item, userInteractions);
        Optional<Action> transitionToLater = createTransitionToLaterAction(day, item, userInteractions);
        Optional<Action> moveUp = createMoveUpAction(items, item, userInteractions);
        Optional<Action> moveDown = createMoveDownAction(items, item, userInteractions);
        Action delete = createDeleteAction(item, userInteractions);

        Actions actions = collate(markComplete, markNotComplete, edit, transitionToToday, transitionToTomorrow, transitionToLater, moveUp, moveDown, delete);
        return new AutoValue_ChunksActions(actions, markComplete, markNotComplete, edit, transitionToToday, transitionToTomorrow, transitionToLater, moveUp, moveDown, delete, day);
    }

    private static Optional<Action> createMarkCompleteAction(final Item item, final ItemUserInteractions userInteractions) {
        if (item.isCompleted()) {
            return Optional.absent();
        }
        return Optional.of(new Action(R.id.action_mark_complete, R.string.action_mark_complete, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserMarkComplete(item);
            }
        }));
    }

    private static Optional<Action> createMarkNotCompleteAction(final Item item, final ItemUserInteractions userInteractions) {
        if (!item.isCompleted()) {
            return Optional.absent();
        }
        return Optional.of(new Action(R.id.action_mark_not_complete, R.string.action_mark_not_complete, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserMarkNotComplete(item);
            }
        }));
    }

    private static Action createEditAction(final Item item, final ItemUserInteractions userInteractions) {
        return new Action(R.id.action_edit, R.string.action_edit, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserEdit(item);
            }
        });
    }

    private static Optional<Action> createTransitionToTodayAction(Day day, final Item item, final ItemUserInteractions userInteractions) {
        if (day == Day.TODAY) {
            return Optional.absent();
        }
        return Optional.of(new Action(R.id.action_move_to_today, R.string.action_move_to_today, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserTransitionItem(item, Day.TODAY);
            }
        }));
    }

    private static Optional<Action> createTransitionToTomorrowAction(Day day, final Item item, final ItemUserInteractions userInteractions) {
        if (day == Day.TOMORROW) {
            return Optional.absent();
        }
        return Optional.of(new Action(R.id.action_move_to_tomorrow, R.string.action_move_to_tomorrow, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserTransitionItem(item, Day.TOMORROW);
            }
        }));
    }

    private static Optional<Action> createTransitionToLaterAction(Day day, final Item item, final ItemUserInteractions userInteractions) {
        if (day == Day.SOMETIME) {
            return Optional.absent();
        }
        return Optional.of(new Action(R.id.action_move_to_later, R.string.action_move_to_later, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserTransitionItem(item, Day.SOMETIME);
            }
        }));
    }

    private static Optional<Action> createMoveUpAction(final Items items, final Item item, final ItemUserInteractions userInteractions) {
        final int originalEntryPosition = items.entries().indexOf(item);
        if (originalEntryPosition == 0) {
            return Optional.absent();
        }
        return Optional.of(new Action(R.id.action_move_up, R.string.action_move_up, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserMove(item, originalEntryPosition - 1);
            }
        }));
    }

    private static Optional<Action> createMoveDownAction(final Items items, final Item item, final ItemUserInteractions userInteractions) {
        final int originalEntryPosition = items.entries().indexOf(item);
        if (originalEntryPosition == items.size() - 1) {
            return Optional.absent();
        }
        return Optional.of(new Action(R.id.action_move_down, R.string.action_move_down, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserMove(item, originalEntryPosition + 1);
            }
        }));
    }

    private static Action createDeleteAction(final Item item, final ItemUserInteractions userInteractions) {
        return new Action(R.id.action_delete, R.string.action_delete, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserRemove(item);
            }
        });
    }

    private static Actions collate(
            Optional<Action> markComplete,
            Optional<Action> markNotComplete,
            Action edit,
            Optional<Action> transitionToToday,
            Optional<Action> transitionToTomorrow,
            Optional<Action> transitionToLater,
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
        if (transitionToToday.isPresent()) {
            actions.add(transitionToToday.get());
        }
        if (transitionToTomorrow.isPresent()) {
            actions.add(transitionToTomorrow.get());
        }
        if (transitionToLater.isPresent()) {
            actions.add(transitionToLater.get());
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

    abstract Optional<Action> transitionToToday();

    abstract Optional<Action> transitionToTomorrow();

    abstract Optional<Action> transitionToLater();

    abstract Optional<Action> moveUp();

    abstract Optional<Action> moveDown();

    abstract Action delete();

    abstract Day currentDay();

    Optional<Action> transitionToPreviousDay() {
        Day day = currentDay();
        switch (day) {
            case TODAY:
                return Optional.absent();
            case TOMORROW:
                return transitionToToday();
            case SOMETIME:
                return transitionToTomorrow();
            default:
                throw new IllegalArgumentException("unknown day: " + day);
        }
    }

    Optional<Action> transitionToNextDay() {
        Day day = currentDay();
        switch (day) {
            case TODAY:
                return transitionToTomorrow();
            case TOMORROW:
                return transitionToLater();
            case SOMETIME:
                return Optional.absent();
            default:
                throw new IllegalArgumentException("unknown day: " + day);
        }
    }

}

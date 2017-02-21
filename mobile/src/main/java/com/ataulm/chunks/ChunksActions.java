package com.ataulm.chunks;

import com.novoda.accessibility.Action;
import com.novoda.accessibility.Actions;

import java.util.Arrays;
import java.util.List;

final class ChunksActions {

    private final Actions actions;
    private final Action moveToTodayAction;
    private final Action moveToTomorrowAction;
    private final Action moveToLaterAction;
    private final Action deleteAction;

    public static ChunksActions create(Day day, final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        Action moveToTodayAction = new Action(R.id.action_move_to_today, R.string.action_move_to_today, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserTransitionEntry(entry, Day.TODAY);
            }
        });
        Action moveToTomorrowAction = new Action(R.id.action_move_to_tomorrow, R.string.action_move_to_tomorrow, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserTransitionEntry(entry, Day.TOMORROW);
            }
        });
        Action moveToLaterAction = new Action(R.id.action_move_to_later, R.string.action_move_to_later, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserTransitionEntry(entry, Day.SOMETIME);
            }
        });
        Action deleteAction = new Action(R.id.action_delete, R.string.action_delete, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserRemove(entry);
            }
        });

        Actions actions = new Actions(collateActions(day, moveToTodayAction, moveToTomorrowAction, moveToLaterAction, deleteAction));
        return new ChunksActions(actions, moveToTodayAction, moveToTomorrowAction, moveToLaterAction, deleteAction);
    }

    private static List<Action> collateActions(Day day, Action moveToTodayAction, Action moveToTomorrowAction, Action moveToLaterAction, Action deleteAction) {
        if (day == Day.TODAY) {
            return Arrays.asList(moveToTomorrowAction, moveToLaterAction, deleteAction);
        } else if (day == Day.TOMORROW) {
            return Arrays.asList(moveToTodayAction, moveToLaterAction, deleteAction);
        } else {
            return Arrays.asList(moveToTodayAction, moveToTomorrowAction, deleteAction);
        }
    }

    private ChunksActions(Actions actions, Action moveToTodayAction, Action moveToTomorrowAction, Action moveToLaterAction, Action deleteAction) {
        this.actions = actions;
        this.moveToTodayAction = moveToTodayAction;
        this.moveToTomorrowAction = moveToTomorrowAction;
        this.moveToLaterAction = moveToLaterAction;
        this.deleteAction = deleteAction;
    }

    public Actions actions() {
        return actions;
    }

    public Action moveToTodayAction() {
        return moveToTodayAction;
    }

    public Action moveToTomorrowAction() {
        return moveToTomorrowAction;
    }

    public Action moveToLaterAction() {
        return moveToLaterAction;
    }

    public Action deleteAction() {
        return deleteAction;
    }

}

package com.ataulm.chunks;

import com.novoda.accessibility.Action;
import com.novoda.accessibility.Actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

final class ChunksActions {

    private final Actions actions;
    private final Action markCompleteAction;
    private final Action markNotCompleteAction;
    private final Action editAction;
    private final Action moveToTodayAction;
    private final Action moveToTomorrowAction;
    private final Action moveToLaterAction;
    private final Action deleteAction;

    public static ChunksActions create(Day day, final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        Action markCompleteAction = new Action(R.id.action_mark_complete, R.string.action_mark_complete, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserMarkComplete(entry);
            }
        });
        Action markNotCompleteAction = new Action(R.id.action_mark_not_complete, R.string.action_mark_not_complete, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserMarkNotComplete(entry);
            }
        });
        Action editAction = new Action(R.id.action_edit, R.string.action_edit, new Runnable() {
            @Override
            public void run() {
                userInteractions.onUserEdit(entry);
            }
        });
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

        Actions actions = new Actions(collateActions(entry, day, markCompleteAction, markNotCompleteAction, editAction, moveToTodayAction, moveToTomorrowAction, moveToLaterAction, deleteAction));
        return new ChunksActions(actions, markCompleteAction, markNotCompleteAction, editAction, moveToTodayAction, moveToTomorrowAction, moveToLaterAction, deleteAction);
    }

    private static List<Action> collateActions(Entry entry, Day day, Action markCompleteAction, Action markNotCompleteAction, Action editAction, Action moveToTodayAction, Action moveToTomorrowAction, Action moveToLaterAction, Action deleteAction) {
        List<Action> actions = new ArrayList<>(Arrays.asList(markCompleteAction, markNotCompleteAction, editAction, moveToTodayAction, moveToTomorrowAction, moveToLaterAction, deleteAction));

        if (entry.isCompleted()) {
            actions.remove(markCompleteAction);
        } else {
            actions.remove(markNotCompleteAction);
        }

        if (day == Day.TODAY) {
            actions.remove(moveToTodayAction);
        } else if (day == Day.TOMORROW) {
            actions.remove(moveToTomorrowAction);
        } else {
            actions.remove(moveToLaterAction);
        }

        return actions;
    }

    private ChunksActions(Actions actions, Action markCompleteAction, Action markNotCompleteAction, Action editAction, Action moveToTodayAction, Action moveToTomorrowAction, Action moveToLaterAction, Action deleteAction) {
        this.actions = actions;
        this.markCompleteAction = markCompleteAction;
        this.markNotCompleteAction = markNotCompleteAction;
        this.editAction = editAction;
        this.moveToTodayAction = moveToTodayAction;
        this.moveToTomorrowAction = moveToTomorrowAction;
        this.moveToLaterAction = moveToLaterAction;
        this.deleteAction = deleteAction;
    }

    public Actions actions() {
        return actions;
    }

    public Action markNotCompleteAction() {
        return markNotCompleteAction;
    }

    public Action markCompleteAction() {
        return markCompleteAction;
    }

    public Action editAction() {
        return editAction;
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

package com.ataulm.chunks

import com.novoda.accessibility.Action
import com.novoda.accessibility.Actions
import java.util.*

data class ChunksActions(
        val actions: Actions,
        val markComplete: Action?,
        val markNotComplete: Action?,
        val edit: Action,
        val transitionToToday: Action?,
        val transitionToTomorrow: Action?,
        val transitionToLater: Action?,
        val moveUp: Action?,
        val moveDown: Action?,
        val delete: Action,
        val currentDay: Day
) {

    fun transitionToPreviousDay(): Action? {
        val day = currentDay
        return when (day) {
            Day.TODAY -> null
            Day.TOMORROW -> transitionToToday
            Day.SOMETIME -> transitionToTomorrow
        }
    }

    fun transitionToNextDay(): Action? {
        val day = currentDay
        return when (day) {
            Day.TODAY -> transitionToTomorrow
            Day.TOMORROW -> transitionToLater
            Day.SOMETIME -> null
        }
    }

    companion object {

        fun create(items: Items, day: Day, item: Item, userInteractions: ItemUserInteractions): ChunksActions {
            val markComplete = createMarkCompleteAction(item, userInteractions)
            val markNotComplete = createMarkNotCompleteAction(item, userInteractions)
            val edit = createEditAction(item, userInteractions)
            val transitionToToday = createTransitionToTodayAction(day, item, userInteractions)
            val transitionToTomorrow = createTransitionToTomorrowAction(day, item, userInteractions)
            val transitionToLater = createTransitionToLaterAction(day, item, userInteractions)
            val moveUp = createMoveUpAction(items, item, userInteractions)
            val moveDown = createMoveDownAction(items, item, userInteractions)
            val delete = createDeleteAction(item, userInteractions)

            val actions = collate(markComplete, markNotComplete, edit, transitionToToday, transitionToTomorrow, transitionToLater, moveUp, moveDown, delete)
            return ChunksActions(actions, markComplete, markNotComplete, edit, transitionToToday, transitionToTomorrow, transitionToLater, moveUp, moveDown, delete, day)
        }

        private fun createMarkCompleteAction(item: Item, userInteractions: ItemUserInteractions): Action? {
            return if (item.isCompleted) {
                null
            } else Action(R.id.action_mark_complete, R.string.action_mark_complete, Runnable { userInteractions.onUserMarkComplete(item) })
        }

        private fun createMarkNotCompleteAction(item: Item, userInteractions: ItemUserInteractions): Action? {
            return if (!item.isCompleted) {
                null
            } else Action(R.id.action_mark_not_complete, R.string.action_mark_not_complete, Runnable { userInteractions.onUserMarkNotComplete(item) })
        }

        private fun createEditAction(item: Item, userInteractions: ItemUserInteractions): Action {
            return Action(R.id.action_edit, R.string.action_edit, Runnable { userInteractions.onUserEdit(item) })
        }

        private fun createTransitionToTodayAction(day: Day, item: Item, userInteractions: ItemUserInteractions): Action? {
            return if (day == Day.TODAY) {
                null
            } else Action(R.id.action_move_to_today, R.string.action_move_to_today, Runnable { userInteractions.onUserTransitionItem(item, Day.TODAY) })
        }

        private fun createTransitionToTomorrowAction(day: Day, item: Item, userInteractions: ItemUserInteractions): Action? {
            return if (day == Day.TOMORROW) {
                null
            } else Action(R.id.action_move_to_tomorrow, R.string.action_move_to_tomorrow, Runnable { userInteractions.onUserTransitionItem(item, Day.TOMORROW) })
        }

        private fun createTransitionToLaterAction(day: Day, item: Item, userInteractions: ItemUserInteractions): Action? {
            return if (day == Day.SOMETIME) {
                null
            } else Action(R.id.action_move_to_later, R.string.action_move_to_later, Runnable { userInteractions.onUserTransitionItem(item, Day.SOMETIME) })
        }

        private fun createMoveUpAction(items: Items, item: Item, userInteractions: ItemUserInteractions): Action? {
            val originalEntryPosition = items.entries.indexOf(item)
            return if (originalEntryPosition == 0) {
                null
            } else Action(R.id.action_move_up, R.string.action_move_up, Runnable { userInteractions.onUserMove(item, originalEntryPosition - 1) })
        }

        private fun createMoveDownAction(items: Items, item: Item, userInteractions: ItemUserInteractions): Action? {
            val originalEntryPosition = items.entries.indexOf(item)
            return if (originalEntryPosition == items.size() - 1) {
                null
            } else Action(R.id.action_move_down, R.string.action_move_down, Runnable { userInteractions.onUserMove(item, originalEntryPosition + 1) })
        }

        private fun createDeleteAction(item: Item, userInteractions: ItemUserInteractions): Action {
            return Action(R.id.action_delete, R.string.action_delete, Runnable { userInteractions.onUserRemove(item) })
        }

        private fun collate(
                markComplete: Action?,
                markNotComplete: Action?,
                edit: Action,
                transitionToToday: Action?,
                transitionToTomorrow: Action?,
                transitionToLater: Action?,
                moveUp: Action?,
                moveDown: Action?,
                delete: Action
        ): Actions {
            val actions = ArrayList<Action>()
            markComplete?.let { actions.add(it) }
            markNotComplete?.let { actions.add(it) }
            actions.add(edit)
            transitionToToday?.let { actions.add(it) }
            transitionToTomorrow?.let { actions.add(it) }
            transitionToLater?.let { actions.add(it) }
            moveUp?.let { actions.add(it) }
            moveDown?.let { actions.add(it) }
            actions.add(delete)
            return Actions(actions)
        }
    }

}

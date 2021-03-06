package com.ataulm.chunks

import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.novoda.accessibility.AccessibilityServices
import com.novoda.accessibility.ActionsAlertDialogCreator

internal class ItemViewHolder(private val itemRowView: ItemRowView) : RecyclerView.ViewHolder(itemRowView) {

    private val itemViewClickActions: ItemViewClickActions = ItemViewClickActions(itemRowView)
    private val accessibilityServices: AccessibilityServices = AccessibilityServices.newInstance(itemRowView.context)
    private val actionsAlertDialogCreator: ActionsAlertDialogCreator = ActionsAlertDialogCreator(itemRowView.context)

    fun bind(item: Item, chunksActions: ChunksActions) {
        val alertDialog = actionsAlertDialogCreator.create(chunksActions.actions)

        itemRowView.checkBox().setOnCheckedChangeListener(null)
        itemRowView.checkBox().isChecked = item.isCompleted
        itemRowView.checkBox().setOnCheckedChangeListener { _, _ -> toggleCompleted(chunksActions) }

        itemRowView.itemTextView().text = item.value

        chunksActions.transitionToNextDay()?.let { action ->
            itemRowView.moveLeftButton().visibility = GONE
            itemRowView.moveRightButton().visibility = VISIBLE
            itemRowView.moveRightButton().setOnClickListener { action.run() }
        }

        chunksActions.transitionToPreviousDay()?.let { action ->
            itemRowView.moveRightButton.visibility = GONE
            itemRowView.moveLeftButton.visibility = VISIBLE
            itemRowView.moveLeftButton.setOnClickListener { action.run() }
        }

        itemRowView.setOnClickListener { alertDialog.show() }
        if (accessibilityServices.isSpokenFeedbackEnabled) {
            itemViewClickActions.setClickListeners(chunksActions.actions)
            itemRowView.moveLeftButton().visibility = GONE
            itemRowView.moveRightButton().visibility = GONE
        }
    }

    private fun toggleCompleted(chunksActions: ChunksActions) {
        (chunksActions.markComplete ?: chunksActions.markNotComplete)?.run()
    }

    companion object {

        fun inflate(parent: ViewGroup): ItemViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val view = layoutInflater.inflate(R.layout.view_chunk_item, parent, false) as ItemRowView
            return ItemViewHolder(view)
        }
    }
}

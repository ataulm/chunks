package com.ataulm.chunks

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import java.util.*

internal class ChunkRecyclerViewAdapter(private val userInteractions: ItemUserInteractions,
                                        private var day: Day?,
                                        private var items: Items?) : RecyclerView.Adapter<ItemViewHolder>() {

    init {
        super.setHasStableIds(true)
    }

    fun update(day: Day, items: Items): Boolean {
        val addedItem = items.size() > this.items!!.size()

        this.day = day
        this.items = items
        notifyDataSetChanged()

        return addedItem
    }

    fun onItemMoving(source: Int, target: Int) {
        val entries = ArrayList(items!!.entries)
        if (source < target) {
            for (i in source until target) {
                Collections.swap(entries, i, i + 1)
            }
        } else {
            for (i in source downTo target + 1) {
                Collections.swap(entries, i, i - 1)
            }
        }
        this.items = Items.create(entries)
        notifyItemMoved(source, target)
    }

    fun onItemMoved(endPosition: Int) {
        val item = items!!.get(endPosition)
        userInteractions.onUserMove(item, endPosition)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder.inflate(parent)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items!![position]
        val chunksActions = ChunksActions.create(items!!, day!!, item, userInteractions)
        holder.bind(item, chunksActions)
    }

    override fun getItemCount(): Int {
        return items!!.size()
    }

    override fun getItemId(position: Int): Long {
        val item = items!![position]
        return item.id.value.hashCode().toLong()
    }

}

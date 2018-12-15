package com.ataulm.chunks.repository

import com.ataulm.chunks.Id
import com.ataulm.chunks.Item

class GsonItemConverter {

    fun convert(item: Item): GsonItem {
        val gsonItem = GsonItem()
        gsonItem.id = item.id().value.toString()
        gsonItem.value = item.value()
        gsonItem.completedTimestamp = if (item.completedTimestamp().isPresent) item.completedTimestamp().get() else null
        return gsonItem
    }

    fun convert(gsonItem: GsonItem): Item {
        return Item.createFrom(
                Id.createFrom(gsonItem.id),
                gsonItem.value,
                gsonItem.completedTimestamp
        )
    }

}

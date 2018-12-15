package com.ataulm.chunks

interface ItemUserInteractions {

    fun onUserMarkComplete(item: Item)

    fun onUserMarkNotComplete(item: Item)

    fun onUserTransitionItem(item: Item, day: Day)

    fun onUserEdit(item: Item)

    fun onUserRemove(item: Item)

    fun onUserMove(item: Item, position: Int)

}

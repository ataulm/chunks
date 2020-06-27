package com.ataulm.chunks.room;

import androidx.room.RoomDatabase;

@androidx.room.Database(entities = {ChunksRoom.Entity.class}, version = 1)
public abstract class ChunksRoomDatabase extends RoomDatabase {

    abstract ChunksRoom.Dao dataAccessObject();
}

package com.ataulm.chunks.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomDatabase;
import android.support.annotation.Nullable;

public class ChunksRoom {

    private static final String TABLE_NAME = "chunks";
    private static final String COLUMN_NAME_PRIMARY_KEY = "primary_key";
    private static final String COLUMN_NAME_JSON = "json";
    // since we want to overwrite the same record each time, we'll hard-code the primary key
    private static final int PRIMARY_KEY = 1;

    @android.arch.persistence.room.Database(entities = {Entity.class}, version = 1)
    public static abstract class Database extends RoomDatabase {

        abstract Dao dataAccessObject();
    }

    @android.arch.persistence.room.Dao
    public interface Dao {

        @Nullable
        @Query("SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_NAME_PRIMARY_KEY + " = " + PRIMARY_KEY)
        Entity readChunks();

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        void insertChunks(Entity chunksRecord);
    }

    @android.arch.persistence.room.Entity(tableName = TABLE_NAME)
    public static class Entity {

        @PrimaryKey
        @ColumnInfo(name = COLUMN_NAME_PRIMARY_KEY)
        private final int primaryKey;

        @ColumnInfo(name = COLUMN_NAME_JSON)
        private final String json;

        @Ignore
            // Room will use the other ctor when unmarshalling from the database
        Entity(String json) {
            this(PRIMARY_KEY, json);
        }

        Entity(int primaryKey, String json) {
            this.primaryKey = primaryKey;
            this.json = json;
        }

        int primaryKey() {
            return primaryKey;
        }

        String json() {
            return json;
        }
    }
}

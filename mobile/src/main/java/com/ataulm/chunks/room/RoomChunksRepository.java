package com.ataulm.chunks.room;

import androidx.room.Room;
import android.content.Context;

import com.ataulm.Optional;
import com.ataulm.chunks.BuildConfig;
import com.ataulm.chunks.Chunks;
import com.ataulm.chunks.repository.ChunksRepository;
import com.ataulm.chunks.repository.GsonChunks;
import com.ataulm.chunks.repository.GsonChunksConverter;
import com.ataulm.chunks.repository.JsonChunksConverter;

public class RoomChunksRepository implements ChunksRepository {

    private static final String DATABASE_NAME = BuildConfig.APPLICATION_ID + ".db";

    public static RoomChunksRepository create(Context context, GsonChunksConverter gsonChunksConverter, JsonChunksConverter jsonChunksConverter) {
        ChunksRoomDatabase database = Room.databaseBuilder(context, ChunksRoomDatabase.class, DATABASE_NAME)
                .allowMainThreadQueries() // :ok_hand:
                .build();
        return new RoomChunksRepository(database.dataAccessObject(), gsonChunksConverter, jsonChunksConverter);
    }

    private final ChunksRoom.Dao dataAccessObject;
    private final GsonChunksConverter gsonChunksConverter;
    private final JsonChunksConverter jsonChunksConverter;

    RoomChunksRepository(ChunksRoom.Dao dataAccessObject,
                         GsonChunksConverter gsonChunksConverter,
                         JsonChunksConverter jsonChunksConverter) {
        this.dataAccessObject = dataAccessObject;
        this.gsonChunksConverter = gsonChunksConverter;
        this.jsonChunksConverter = jsonChunksConverter;
    }

    @Override
    public Optional<Chunks> getChunks() {
        ChunksRoom.Entity chunks = dataAccessObject.readChunks();
        if (chunks == null) {
            return Optional.absent();
        } else {
            GsonChunks gsonChunks = jsonChunksConverter.convert(chunks.json());
            return Optional.of(gsonChunksConverter.convert(gsonChunks));
        }
    }

    @Override
    public void persist(Chunks chunks) {
        GsonChunks gsonChunks = gsonChunksConverter.convert(chunks);
        String json = jsonChunksConverter.convert(gsonChunks);

        ChunksRoom.Entity entity = new ChunksRoom.Entity(json);
        dataAccessObject.insertChunks(entity);
    }
}

package com.ataulm.chunks;

import android.content.Context;
import android.content.SharedPreferences;

import com.ataulm.Optional;
import com.ataulm.chunks.repository.ChunksRepository;
import com.ataulm.chunks.repository.GsonChunks;
import com.ataulm.chunks.repository.GsonChunksConverter;
import com.ataulm.chunks.repository.JsonChunksConverter;

/**
 * @deprecated use {@link com.ataulm.chunks.room.RoomChunksRepository}
 */
@Deprecated
final class SharedPreferencesChunksRepository implements ChunksRepository {

    private static final String PREFS_NAME = "db";
    private static final String ALL_ENTRIES = "all_entries";

    private final SharedPreferences sharedPreferences;
    private final GsonChunksConverter gsonChunksConverter;
    private final JsonChunksConverter jsonChunksConverter;

    static SharedPreferencesChunksRepository create(Context context, GsonChunksConverter gsonChunksConverter, JsonChunksConverter jsonChunksConverter) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return new SharedPreferencesChunksRepository(sharedPreferences, gsonChunksConverter, jsonChunksConverter);
    }

    private SharedPreferencesChunksRepository(SharedPreferences sharedPreferences, GsonChunksConverter gsonChunksConverter, JsonChunksConverter jsonChunksConverter) {
        this.sharedPreferences = sharedPreferences;
        this.gsonChunksConverter = gsonChunksConverter;
        this.jsonChunksConverter = jsonChunksConverter;
    }

    @Override
    public Optional<Chunks> getChunks() {
        if (sharedPreferences.contains(ALL_ENTRIES)) {
            String json = sharedPreferences.getString(ALL_ENTRIES, "");
            return Optional.of(chunksFromJson(json));
        } else {
            return Optional.absent();
        }
    }

    private Chunks chunksFromJson(String json) {
        GsonChunks gsonChunks = jsonChunksConverter.convert(json);
        return gsonChunksConverter.convert(gsonChunks);
    }

    @Override
    public void persist(Chunks chunks) {
        throw new IllegalStateException("This class is deprecated, use RoomChunksRepository to persist new data.");
    }

    void clearRepository() {
        sharedPreferences.edit().clear().apply();
    }
}

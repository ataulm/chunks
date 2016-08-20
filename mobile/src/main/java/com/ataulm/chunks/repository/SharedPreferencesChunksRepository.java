package com.ataulm.chunks.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.ataulm.chunks.ChunkDate;
import com.ataulm.chunks.Chunks;
import com.ataulm.chunks.SystemClock;

public final class SharedPreferencesChunksRepository implements ChunksRepository {

    private static final String PREFS_NAME = "db";
    private static final String ALL_ENTRIES = "all_entries";

    private final SharedPreferences sharedPreferences;
    private final GsonChunksConverter gsonChunksConverter;
    private final JsonChunksConverter jsonChunksConverter;

    public static SharedPreferencesChunksRepository create(Context context, GsonChunksConverter gsonChunksConverter, JsonChunksConverter jsonChunksConverter) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return new SharedPreferencesChunksRepository(sharedPreferences, gsonChunksConverter, jsonChunksConverter);
    }

    private SharedPreferencesChunksRepository(SharedPreferences sharedPreferences, GsonChunksConverter gsonChunksConverter, JsonChunksConverter jsonChunksConverter) {
        this.sharedPreferences = sharedPreferences;
        this.gsonChunksConverter = gsonChunksConverter;
        this.jsonChunksConverter = jsonChunksConverter;
    }

    @Override
    public Chunks getChunks() {
        if (sharedPreferences.contains(ALL_ENTRIES)) {
            String json = sharedPreferences.getString(ALL_ENTRIES, "");
            return chunksFromJson(json);
        } else {
            return Chunks.empty(ChunkDate.create(new SystemClock()));
        }
    }

    private Chunks chunksFromJson(String json) {
        GsonChunks gsonChunks = jsonChunksConverter.convert(json);
        return gsonChunksConverter.convert(gsonChunks);
    }

    @Override
    public void persist(Chunks chunks) {
        GsonChunks gsonChunks = gsonChunksConverter.convert(chunks);
        String json = jsonChunksConverter.convert(gsonChunks);

        sharedPreferences.edit()
                .putString(ALL_ENTRIES, json)
                .apply();
    }

}

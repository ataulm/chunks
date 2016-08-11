package com.ataulm.basic.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.ataulm.basic.Chunks;

public final class SharedPreferencesChunksRepository implements ChunksRepository {

    private static final String PREFS_NAME = "db";
    private static final String ALL_ENTRIES = "all_entries";

    private final SharedPreferences sharedPreferences;
    private final GsonEntriesConverter gsonEntriesConverter;
    private final JsonEntriesConverter jsonEntriesConverter;

    public static SharedPreferencesChunksRepository create(Context context, GsonEntriesConverter gsonEntriesConverter, JsonEntriesConverter jsonEntriesConverter) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return new SharedPreferencesChunksRepository(sharedPreferences, gsonEntriesConverter, jsonEntriesConverter);
    }

    private SharedPreferencesChunksRepository(SharedPreferences sharedPreferences, GsonEntriesConverter gsonEntriesConverter, JsonEntriesConverter jsonEntriesConverter) {
        this.sharedPreferences = sharedPreferences;
        this.gsonEntriesConverter = gsonEntriesConverter;
        this.jsonEntriesConverter = jsonEntriesConverter;
    }

    @Override
    public Chunks getChunks() {
        if (sharedPreferences.contains(ALL_ENTRIES)) {
            String json = sharedPreferences.getString(ALL_ENTRIES, "");
            return chunksFromJson(json);
        } else {
            return Chunks.empty();
        }
    }

    private Chunks chunksFromJson(String json) {
        GsonEntries gsonEntries = jsonEntriesConverter.convert(json);
        return gsonEntriesConverter.convert(gsonEntries);
    }

    @Override
    public void persist(Chunks chunks) {
        GsonEntries gsonEntries = gsonEntriesConverter.convert(chunks);
        String json = jsonEntriesConverter.convert(gsonEntries);

        sharedPreferences.edit()
                .putString(ALL_ENTRIES, json)
                .apply();
    }

}

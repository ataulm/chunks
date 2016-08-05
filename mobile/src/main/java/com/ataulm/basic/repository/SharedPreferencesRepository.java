package com.ataulm.basic.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.ataulm.basic.Entries;
import com.ataulm.basic.GsonEntries;
import com.ataulm.basic.Optional;

public final class SharedPreferencesRepository implements Repository {

    private static final String PREFS_NAME = "db";
    private static final String ALL_ENTRIES = "all_entries";

    private final SharedPreferences sharedPreferences;
    private final GsonEntriesConverter gsonEntriesConverter;
    private final JsonEntriesConverter jsonEntriesConverter;

    public static SharedPreferencesRepository create(Context context, GsonEntriesConverter gsonEntriesConverter, JsonEntriesConverter jsonEntriesConverter) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return new SharedPreferencesRepository(sharedPreferences, gsonEntriesConverter, jsonEntriesConverter);
    }

    private SharedPreferencesRepository(SharedPreferences sharedPreferences, GsonEntriesConverter gsonEntriesConverter, JsonEntriesConverter jsonEntriesConverter) {
        this.sharedPreferences = sharedPreferences;
        this.gsonEntriesConverter = gsonEntriesConverter;
        this.jsonEntriesConverter = jsonEntriesConverter;
    }

    @Override
    public Optional<Entries> fetchEntries() {
        if (sharedPreferences.contains(ALL_ENTRIES)) {
            String json = sharedPreferences.getString(ALL_ENTRIES, elseThrowException());
            return entriesFromJson(json);
        } else {
            return Optional.absent();
        }
    }

    private Optional<Entries> entriesFromJson(String json) {
        GsonEntries gsonEntries = jsonEntriesConverter.convert(json);
        Entries entries = gsonEntriesConverter.convert(gsonEntries);
        return Optional.of(entries);
    }

    private String elseThrowException() {
        throw new IllegalArgumentException("no value found, did you check if it was present?");
    }

    @Override
    public void persistEntries(Entries entries) {
        GsonEntries gsonEntries = gsonEntriesConverter.convert(entries);
        String json = jsonEntriesConverter.convert(gsonEntries);

        sharedPreferences.edit()
                .putString("all_entries", json)
                .apply();
    }

}

package com.ataulm.chunks.repository;

import com.google.gson.Gson;

public class JsonEntriesConverter {

    private final Gson gson;

    public JsonEntriesConverter(Gson gson) {
        this.gson = gson;
    }

    public String convert(GsonEntries entry) {
        return gson.toJson(entry, GsonEntries.class);
    }

    public GsonEntries convert(String entry) {
        return gson.fromJson(entry, GsonEntries.class);
    }

}

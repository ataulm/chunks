package com.ataulm.basic;

import com.google.gson.Gson;

public class JsonEntryConverter {

    private final Gson gson;

    public JsonEntryConverter(Gson gson) {
        this.gson = gson;
    }

    public String convert(GsonEntry entry) {
        return gson.toJson(entry, GsonEntry.class);
    }

    public GsonEntry convert(String entry) {
        return gson.fromJson(entry, GsonEntry.class);
    }

}

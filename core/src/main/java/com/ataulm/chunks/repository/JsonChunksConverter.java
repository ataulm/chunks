package com.ataulm.chunks.repository;

import com.google.gson.Gson;

public class JsonChunksConverter {

    private final Gson gson;

    public JsonChunksConverter(Gson gson) {
        this.gson = gson;
    }

    public String convert(GsonChunks gsonChunks) {
        return gson.toJson(gsonChunks, GsonChunks.class);
    }

    public GsonChunks convert(String gsonChunks) {
        return gson.fromJson(gsonChunks, GsonChunks.class);
    }

}

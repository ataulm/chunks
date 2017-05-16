package com.ataulm.chunks;

import android.app.Application;

import com.ataulm.AndroidLog;
import com.ataulm.chunks.repository.ChunksRepository;
import com.ataulm.chunks.repository.GsonChunkConverter;
import com.ataulm.chunks.repository.GsonChunksConverter;
import com.ataulm.chunks.repository.GsonEntryConverter;
import com.ataulm.chunks.repository.JsonChunksConverter;
import com.ataulm.chunks.repository.SharedPreferencesChunksRepository;
import com.google.gson.Gson;

public class ChunksApplication extends Application {

    private ChunksService chunksService;

    @Override
    public void onCreate() {
        super.onCreate();
        Toaster.init(this);

        GsonChunksConverter gsonChunksConverter = new GsonChunksConverter(new GsonChunkConverter(new GsonEntryConverter()));
        JsonChunksConverter jsonChunksConverter = new JsonChunksConverter(new Gson());
        ChunksRepository chunksRepository = SharedPreferencesChunksRepository.create(this, gsonChunksConverter, jsonChunksConverter);
        chunksService = new ChunksService(chunksRepository, new ChunksEditor(), new SystemClock(), new AndroidLog());
    }

    public ChunksService getChunksService() {
        return chunksService;
    }

}

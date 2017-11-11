package com.ataulm.chunks;

import android.app.Application;

import com.ataulm.AndroidLog;
import com.ataulm.chunks.repository.ChunksRepository;
import com.ataulm.chunks.repository.GsonChunkConverter;
import com.ataulm.chunks.repository.GsonChunksConverter;
import com.ataulm.chunks.repository.GsonItemConverter;
import com.ataulm.chunks.repository.JsonChunksConverter;
import com.ataulm.chunks.repository.SharedPreferencesChunksRepository;
import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.core.CrashlyticsCore;
import com.google.gson.Gson;

import io.fabric.sdk.android.Fabric;

public class ChunksApplication extends Application {

    private ChunksService chunksService;

    @Override
    public void onCreate() {
        super.onCreate();
        initializeFabric();

        Toaster.init(this);

        GsonChunksConverter gsonChunksConverter = new GsonChunksConverter(new GsonChunkConverter(new GsonItemConverter()));
        JsonChunksConverter jsonChunksConverter = new JsonChunksConverter(new Gson());

        // TODO: migrate data from SharedPrefs to SQLite
        // probably involved modifying the repository so it's easier to understand when it's empty
        // by returning Optional<Chunks> vs Chunks.empty
        //
        // 1. pull data from SharedPrefs
        // 2. if empty, do nowt, return Room version
        // 3. else, persist chunks using Room version, clear SharedPrefs and return Room version

        ChunksRepository chunksRepository = SharedPreferencesChunksRepository.create(this, gsonChunksConverter, jsonChunksConverter);
        chunksService = new ChunksService(chunksRepository, new ChunksEditor(), new SystemClock(), new AndroidLog());
    }

    private void initializeFabric() {
        if (!BuildConfig.SHOULD_ENABLE_FABRIC) {
            return;
        }
        CrashlyticsCore core = new CrashlyticsCore.Builder().build();
        Crashlytics crashlytics = new Crashlytics.Builder().core(core).build();
        Fabric.with(this, crashlytics);
    }

    public ChunksService getChunksService() {
        return chunksService;
    }
}

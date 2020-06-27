package com.ataulm.chunks;

import android.app.Application;

import com.ataulm.AndroidLog;
import com.ataulm.Optional;
import com.ataulm.chunks.repository.ChunksRepository;
import com.ataulm.chunks.repository.GsonChunkConverter;
import com.ataulm.chunks.repository.GsonChunksConverter;
import com.ataulm.chunks.repository.GsonItemConverter;
import com.ataulm.chunks.repository.JsonChunksConverter;
import com.ataulm.chunks.room.RoomChunksRepository;
import com.google.gson.Gson;

public class ChunksApplication extends Application {

    private ChunksService chunksService;

    @Override
    public void onCreate() {
        super.onCreate();

        Toaster.init(this);

        GsonChunksConverter gsonChunksConverter = new GsonChunksConverter(new GsonChunkConverter(new GsonItemConverter()));
        JsonChunksConverter jsonChunksConverter = new JsonChunksConverter(new Gson());

        ChunksRepository chunksRepository = RoomChunksRepository.create(this, gsonChunksConverter, jsonChunksConverter);
        migrateDataFromSharedPrefsToNewRepository(gsonChunksConverter, jsonChunksConverter, chunksRepository);
        chunksService = new ChunksService(chunksRepository, new ChunksEditor(), new SystemClock(), new AndroidLog());
    }

    @SuppressWarnings("deprecation") // SharedPrefRepo deprecated but we gotta migrate data
    private void migrateDataFromSharedPrefsToNewRepository(GsonChunksConverter gsonChunksConverter, JsonChunksConverter jsonChunksConverter, ChunksRepository repository) {
        SharedPreferencesChunksRepository sharedPrefsRepo = SharedPreferencesChunksRepository.create(this, gsonChunksConverter, jsonChunksConverter);
        Optional<Chunks> chunks = sharedPrefsRepo.getChunks();
        if (chunks.isPresent()) {
            repository.persist(chunks.get()); // TODO: this should be off the main thread
            sharedPrefsRepo.clearRepository();
        }
    }

    public ChunksService getChunksService() {
        return chunksService;
    }
}

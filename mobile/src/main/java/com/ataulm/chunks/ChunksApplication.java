package com.ataulm.chunks;

import android.app.Application;

import com.ataulm.AndroidLog;
import com.ataulm.chunks.repository.GsonEntriesConverter;
import com.ataulm.chunks.repository.GsonEntryConverter;
import com.ataulm.chunks.repository.JsonEntriesConverter;
import com.ataulm.chunks.repository.ChunksRepository;
import com.ataulm.chunks.repository.SharedPreferencesChunksRepository;
import com.google.gson.Gson;

public class ChunksApplication extends Application {

    private ChunksService chunksService;

    @Override
    public void onCreate() {
        super.onCreate();
        Toaster.init(this);

        GsonEntriesConverter gsonEntriesConverter = new GsonEntriesConverter(new GsonEntryConverter());
        JsonEntriesConverter jsonEntriesConverter = new JsonEntriesConverter(new Gson());
        ChunksRepository chunksRepository = SharedPreferencesChunksRepository.create(this, gsonEntriesConverter, jsonEntriesConverter);
        chunksService = new ChunksService(chunksRepository, new AndroidLog());
    }

    public ChunksService getChunksService() {
        return chunksService;
    }

}

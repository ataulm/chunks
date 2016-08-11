package com.ataulm.basic;

import android.app.Application;

import com.ataulm.AndroidLog;
import com.ataulm.basic.repository.GsonEntriesConverter;
import com.ataulm.basic.repository.GsonEntryConverter;
import com.ataulm.basic.repository.JsonEntriesConverter;
import com.ataulm.basic.repository.ChunksRepository;
import com.ataulm.basic.repository.SharedPreferencesChunksRepository;
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

package com.ataulm.chunks.repository;

import com.ataulm.chunks.Chunks;

public class GsonChunksConverter {

    private final GsonChunkConverter chunkConverter;

    public GsonChunksConverter(GsonChunkConverter chunkConverter) {
        this.chunkConverter = chunkConverter;
    }

    public GsonChunks convert(Chunks chunks) {
        GsonChunks gsonChunks = new GsonChunks();
        gsonChunks.lastShuffledDate = chunks.lastShuffledTimestamp();
        gsonChunks.today = chunkConverter.convert(chunks.today());
        gsonChunks.tomorrow = chunkConverter.convert(chunks.tomorrow());
        return gsonChunks;
    }

    public Chunks convert(GsonChunks gsonChunks) {
        return Chunks.create(
                gsonChunks.lastShuffledDate,
                chunkConverter.convert(gsonChunks.today),
                chunkConverter.convert(gsonChunks.tomorrow)
        );
    }

}

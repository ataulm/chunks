package com.ataulm.chunks;

import static com.ataulm.chunks.ChunkFixtures.aChunk;

public final class ChunksFixtures {

    private String lastShuffledTimestamp;
    private Chunk yesterday;
    private Chunk today;
    private Chunk tomorrow;

    public static ChunksFixtures aChunks() {
        return new ChunksFixtures(
                "0",
                aChunk().get(),
                aChunk().get(),
                aChunk().get()
        );
    }

    private ChunksFixtures(String lastShuffledTimestamp, Chunk yesterday, Chunk today, Chunk tomorrow) {
        this.lastShuffledTimestamp = lastShuffledTimestamp;
        this.yesterday = yesterday;
        this.today = today;
        this.tomorrow = tomorrow;
    }

    public ChunksFixtures withLastShuffledTimestamp(String lastShuffledTimestamp) {
        this.lastShuffledTimestamp = lastShuffledTimestamp;
        return this;
    }

    public ChunksFixtures withYesterday(Chunk yesterday) {
        this.yesterday = yesterday;
        return this;
    }

    public ChunksFixtures withToday(Chunk today) {
        this.today = today;
        return this;
    }

    public ChunksFixtures withTomorrow(Chunk tomorrow) {
        this.tomorrow = tomorrow;
        return this;
    }

    public Chunks get() {
        return Chunks.create(lastShuffledTimestamp, today, tomorrow);
    }

}

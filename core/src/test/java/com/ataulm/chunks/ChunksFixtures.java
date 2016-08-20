package com.ataulm.chunks;

import static com.ataulm.chunks.ChunkFixtures.aChunk;

public final class ChunksFixtures {

    private ChunkDate todaysDate;
    private Chunk today;
    private Chunk tomorrow;

    public static ChunksFixtures aChunks() {
        return new ChunksFixtures(
                ChunkDate.create(0),
                aChunk().get(),
                aChunk().get()
        );
    }

    private ChunksFixtures(ChunkDate todaysDate, Chunk today, Chunk tomorrow) {
        this.todaysDate = todaysDate;
        this.today = today;
        this.tomorrow = tomorrow;
    }

    public ChunksFixtures withTodaysDate(ChunkDate todaysDate) {
        this.todaysDate = todaysDate;
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
        return Chunks.create(todaysDate, today, tomorrow);
    }

}

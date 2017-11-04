package com.ataulm.chunks;

import static com.ataulm.chunks.ItemsFixtures.items;

public final class ChunksFixtures {

    private ChunkDate todaysDate;
    private Items today;
    private Items tomorrow;
    private Items sometime;

    public static ChunksFixtures aChunks() {
        return new ChunksFixtures(
                ChunkDate.create(0),
                items().get(),
                items().get(),
                items().get()
        );
    }

    private ChunksFixtures(ChunkDate todaysDate, Items today, Items tomorrow, Items sometime) {
        this.todaysDate = todaysDate;
        this.today = today;
        this.tomorrow = tomorrow;
        this.sometime = sometime;
    }

    public ChunksFixtures withTodaysDate(ChunkDate todaysDate) {
        this.todaysDate = todaysDate;
        return this;
    }

    public ChunksFixtures withToday(Items today) {
        this.today = today;
        return this;
    }

    public ChunksFixtures withTomorrow(Items tomorrow) {
        this.tomorrow = tomorrow;
        return this;
    }

    public ChunksFixtures withSometime(Items sometime) {
        this.sometime = sometime;
        return this;
    }

    public Chunks get() {
        return Chunks.create(todaysDate, today, tomorrow, sometime);
    }

}

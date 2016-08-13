package com.ataulm.chunks.repository;

import com.ataulm.chunks.Chunks;

public interface ChunksRepository {

    Chunks getChunks();

    void persist(Chunks chunks);

}

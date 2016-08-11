package com.ataulm.basic.repository;

import com.ataulm.basic.Chunks;

public interface ChunksRepository {

    Chunks getChunks();

    void persist(Chunks chunks);

}

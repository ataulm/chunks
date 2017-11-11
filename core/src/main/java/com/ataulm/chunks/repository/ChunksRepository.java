package com.ataulm.chunks.repository;

import com.ataulm.Optional;
import com.ataulm.chunks.Chunks;

public interface ChunksRepository {

    Optional<Chunks> getChunks();

    void persist(Chunks chunks);
}

package com.ataulm.basic.repository;

import com.ataulm.basic.Entries;
import com.ataulm.basic.Optional;

public interface Repository {

    Optional<Entries> fetchEntries();

    void persistEntries(Entries entries);

}

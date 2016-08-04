package com.ataulm.basic;

import com.google.auto.value.AutoValue;

import java.util.UUID;

@AutoValue
public abstract class Id {

    public static Id create() {
        UUID uuid = UUID.randomUUID();
        return new AutoValue_Id(uuid);
    }

    public static Id createFrom(String rawUuid) {
        UUID uuid = UUID.fromString(rawUuid);
        return new AutoValue_Id(uuid);
    }

    public abstract UUID value();

}


package com.ataulm.basic;

import com.google.auto.value.AutoValue;

import java.util.List;

@AutoValue
public abstract class Entries {

    public abstract List<Entry> list();

    public abstract String value();

}


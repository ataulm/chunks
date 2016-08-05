package com.ataulm.basic;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonEntries {

    @SerializedName("modified_timestamp")
    public String modifiedTimestamp;

    @SerializedName("entries")
    public List<GsonEntry> entries;

}

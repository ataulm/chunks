package com.ataulm.chunks.repository;

import com.google.gson.annotations.SerializedName;

public class GsonEntry {

    @SerializedName("id")
    public String id;

    @SerializedName("value")
    public String value;

    @SerializedName("day")
    public String day;

    @SerializedName("completed_timestamp")
    public String completedTimestamp;

}

package com.ataulm.chunks.repository;

import com.google.gson.annotations.SerializedName;

public class GsonChunks {

    @SerializedName("todays_date")
    public String todays_date;

    @SerializedName("today")
    public GsonChunk today;

    @SerializedName("tomorrow")
    public GsonChunk tomorrow;

    @SerializedName("sometime")
    public GsonChunk sometime;

}

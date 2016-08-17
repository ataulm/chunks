package com.ataulm.chunks.repository;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonChunks {

    @SerializedName("last_shuffled_date")
    public String lastShuffledDate;

    @SerializedName("today")
    public GsonChunk today;

    @SerializedName("tomorrow")
    public GsonChunk tomorrow;

}

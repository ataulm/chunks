package com.ataulm.chunks.repository;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonChunk {
    @SerializedName("entries")
    List<GsonEntry> entries;
}

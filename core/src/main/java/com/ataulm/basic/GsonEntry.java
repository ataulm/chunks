package com.ataulm.basic;

import com.google.gson.annotations.SerializedName;

public class GsonEntry {

    @SerializedName("id")
    String id;

    @SerializedName("value")
    String value;

    @SerializedName("day")
    String day;

    @SerializedName("completed_timestamp")
    String completedTimestamp;

}

package com.ataulm.chunks;

import com.google.auto.value.AutoValue;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@AutoValue
public abstract class ChunkDate {

    public static ChunkDate create(Clock clock) {
        return create(clock.getCurrentTime());
    }

    public static ChunkDate create(long timestamp) {
        return new AutoValue_ChunkDate(timestamp);
    }

    public abstract long timestamp();

    public boolean isSameDayAs(ChunkDate date) {
        return date.getDayOfYear() == getDayOfYear()
                && date.getYear() == getYear();
    }

    private int getYear() {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(timestamp());
        return dateCalendar.get(Calendar.YEAR);
    }

    private int getDayOfYear() {
        Calendar dateCalendar = Calendar.getInstance();
        dateCalendar.setTimeInMillis(timestamp());
        return dateCalendar.get(Calendar.DAY_OF_YEAR);
    }

}

package com.ataulm.chunks;

import java.util.Objects;

import androidx.annotation.StringRes;

class DayUiModel {

    @StringRes
    final int title;
    final Day day;
    final Items items;

    DayUiModel(int title, Day day, Items items) {
        this.title = title;
        this.day = day;
        this.items = items;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DayUiModel that = (DayUiModel) o;
        return title == that.title &&
                day == that.day &&
                Objects.equals(items, that.items);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title, day, items);
    }
}

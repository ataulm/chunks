package com.ataulm.chunks;

public class DayToPagePositionMapper {

    private static final int PAGE_TODAY = 0;
    private static final int PAGE_TOMORROW = 1;

    public static Day getDayFor(int pagePosition) {
        switch (pagePosition) {
            case PAGE_TODAY:
                return Day.TODAY;
            case PAGE_TOMORROW:
                return Day.TOMORROW;
            default:
                throw new IllegalStateException("unexpected pagePosition: " + pagePosition);
        }
    }

    public static int getPageFor(Day day) {
        switch (day) {
            case TODAY:
                return PAGE_TODAY;
            case TOMORROW:
                return PAGE_TOMORROW;
            default:
                throw new IllegalStateException("unexpected day: " + day);
        }
    }

}

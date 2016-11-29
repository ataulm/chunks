package com.ataulm.chunks;

public class DayToPagePositionMapper {

    private static final int PAGE_TODAY = 0;
    private static final int PAGE_TOMORROW = 1;
    private static final int PAGE_SOMETIME = 2;

    public static Day getDayFor(int pagePosition) {
        switch (pagePosition) {
            case PAGE_TODAY:
                return Day.TODAY;
            case PAGE_TOMORROW:
                return Day.TOMORROW;
            case PAGE_SOMETIME:
                return Day.SOMETIME;
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
            case SOMETIME:
                return PAGE_SOMETIME;
            default:
                throw new IllegalStateException("unexpected day: " + day);
        }
    }

}

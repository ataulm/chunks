package com.ataulm.chunks;

public class DayToPagePositionMapper {

    public static Day getDayFor(int pagePosition) {
        switch (pagePosition) {
            case 0:
                return Day.YESTERDAY;
            case 1:
                return Day.TODAY;
            case 2:
                return Day.TOMORROW;
            default:
                throw new IllegalStateException("unexpected pagePosition: " + pagePosition);
        }
    }

    public static int getPageFor(Day day) {
        switch (day) {
            case YESTERDAY:
                return 0;
            case TODAY:
                return 1;
            case TOMORROW:
                return 2;
            default:
                throw new IllegalStateException("unexpected day: " + day);
        }
    }

}

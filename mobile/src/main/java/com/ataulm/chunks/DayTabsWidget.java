package com.ataulm.chunks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DayTabsWidget extends LinearLayout {

    @BindView(R.id.day_tabs_yesterday)
    View yesterdayView;

    @BindView(R.id.day_tabs_today)
    View todayView;

    @BindView(R.id.day_tabs_tomorrow)
    View tomorrowView;

    public DayTabsWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_day_tabs, this);
        ButterKnife.bind(this);
    }

    public void bind(final Listener listener) {
        yesterdayView.setOnClickListener(createListenerFor(Day.YESTERDAY, listener));
        todayView.setOnClickListener(createListenerFor(Day.TODAY, listener));
        tomorrowView.setOnClickListener(createListenerFor(Day.TOMORROW, listener));
    }

    private OnClickListener createListenerFor(final Day day, final Listener listener) {
        return new OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.onClick(day);
            }

        };
    }

    public interface Listener {

        void onClick(Day day);

    }

}

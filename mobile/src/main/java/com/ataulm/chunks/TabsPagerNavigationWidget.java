package com.ataulm.chunks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TabsPagerNavigationWidget extends LinearLayout {

    @BindView(R.id.day_tabs_today)
    View todayView;

    @BindView(R.id.day_tabs_tomorrow)
    View tomorrowView;

    public TabsPagerNavigationWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_day_tabs, this);
        ButterKnife.bind(this);
    }

    public void bind(Listener listener) {
        todayView.setOnClickListener(createListenerFor(Day.TODAY, listener));
        tomorrowView.setOnClickListener(createListenerFor(Day.TOMORROW, listener));
    }

    public void display(Day day) {
        switch (day) {
            case TODAY:
                activate(todayView);
                break;
            case TOMORROW:
                activate(tomorrowView);
                break;
            default:
                throw new IllegalArgumentException("unsupported day: " + day);
        }
    }

    private void activate(View view) {
        todayView.setActivated(todayView.equals(view));
        tomorrowView.setActivated(tomorrowView.equals(view));
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

package com.ataulm.chunks;

import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

class OnPageChangeListenerDelegate implements ViewPager.OnPageChangeListener {

    private final List<ViewPager.OnPageChangeListener> listeners = new ArrayList<>();

    public void register(ViewPager.OnPageChangeListener listener) {
        if (listeners.contains(listener)) {
            return;
        }
        listeners.add(listener);
    }

    public void deregister(ViewPager.OnPageChangeListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        for (ViewPager.OnPageChangeListener listener : listeners) {
            listener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        for (ViewPager.OnPageChangeListener listener : listeners) {
            listener.onPageSelected(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        for (ViewPager.OnPageChangeListener listener : listeners) {
            listener.onPageScrollStateChanged(state);
        }
    }
}

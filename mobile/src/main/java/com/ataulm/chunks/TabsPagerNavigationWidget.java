package com.ataulm.chunks;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;

import com.novoda.landingstrip.LandingStrip;

public class TabsPagerNavigationWidget extends LandingStrip {

    public TabsPagerNavigationWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void bind(ViewPager viewPager) {
        if (viewPager.getAdapter() == null) {
            throw new IllegalArgumentException("can only bind after ViewPager has adapter attached.");
        }
        attach(viewPager);
    }

}

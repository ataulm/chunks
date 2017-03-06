package com.ataulm.chunks;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

// TODO: move to library
public class ViewActivity extends Activity {

    public static final String EXTRA_LAYOUT_ID = ViewActivity.class.getName() + ".EXTRA_LAYOUT_ID";

    private View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int layout = getIntent().getIntExtra(EXTRA_LAYOUT_ID, 0);
        view = getLayoutInflater().inflate(layout, null, false);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        setContentView(view, params);
    }

    public View getView() {
        return ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
    }
}

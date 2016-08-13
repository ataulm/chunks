package com.ataulm.chunks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryInputView extends LinearLayout {

    @BindView(R.id.entry_input_edit_text)
    EditText inputEditText;

    @BindView(R.id.entry_input_button_add)
    Button addButton;

    public EntryInputView(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_entry_input, this);
        ButterKnife.bind(this);
    }

    public void bind(final Listener listener) {
        addButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                String s = inputEditText.getText().toString();
                if (s.length() > 0) {
                    listener.onClickAddEntry(s);
                }
            }
        });
    }

    public interface Listener {

        void onClickAddEntry(String entry);

    }

}

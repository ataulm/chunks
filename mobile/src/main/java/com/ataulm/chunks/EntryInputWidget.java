package com.ataulm.chunks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryInputWidget extends LinearLayout {

    @BindView(R.id.entry_input_edit_text)
    EditText inputEditText;

    @BindView(R.id.entry_input_button_add)
    Button addButton;

    public EntryInputWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_entry_input, this);
        ButterKnife.bind(this);
    }

    public void bind(final EntryInputUserInteractions entryInputUserInteractions, final Day day) {
        addButton.setOnClickListener(
                new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String entry = inputEditText.getText().toString();
                        if (entry.length() > 0) {
                            entryInputUserInteractions.onUserAddEntry(entry, day);
                            inputEditText.setText(null);
                        }
                    }
                }
        );
    }

}

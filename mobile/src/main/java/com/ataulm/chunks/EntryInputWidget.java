package com.ataulm.chunks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryInputWidget extends LinearLayout {

    @BindView(R.id.entry_input_edit_text)
    EditText inputEditText;

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

    public void bind(final EntryInputUserInteractions userInteractions, final Day day) {
        setEnterKeyListenerToAddEntryAndPreventMultilineInput(userInteractions, day);
    }

    private void setEnterKeyListenerToAddEntryAndPreventMultilineInput(final EntryInputUserInteractions userInteractions, final Day day) {
        inputEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (enterOrDone(actionId)) {
                    addStack(userInteractions, day);
                    return true;
                }
                return false;
            }

            private boolean enterOrDone(int actionId) {
                return actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_NULL;
            }
        });

        inputEditText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                boolean enterPressed = keyCode == KeyEvent.KEYCODE_ENTER;

                if (enterPressed) {
                    addStack(userInteractions, day);
                    return true;
                }

                return false;
            }
        });
    }

    private void addStack(EntryInputUserInteractions userInteractions, Day day) {
        String entry = inputEditText.getText().toString();
        if (entry.length() > 0) {
            userInteractions.onUserAddEntry(entry, day);
            inputEditText.setText(null);
        }
    }

}

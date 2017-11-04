package com.ataulm.chunks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ItemInputWidget extends FrameLayout {

    @BindView(R.id.item_input_edit_text)
    EditText inputEditText;

    public ItemInputWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_item_input, this);
        ButterKnife.bind(this);
    }

    public void setText(String text) {
        inputEditText.append(text);
    }

    public void update(ItemInputUserInteractions userInteractions, Day day) {
        setEnterKeyListenerToAddItemAndPreventMultilineInput(userInteractions, day);
    }

    private void setEnterKeyListenerToAddItemAndPreventMultilineInput(final ItemInputUserInteractions userInteractions, final Day day) {
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

    private void addStack(ItemInputUserInteractions userInteractions, Day day) {
        String item = inputEditText.getText().toString();
        if (item.length() > 0) {
            userInteractions.onUserAddItem(item, day);
            inputEditText.setText(null);
        }
    }

}

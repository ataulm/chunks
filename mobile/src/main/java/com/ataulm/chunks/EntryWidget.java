package com.ataulm.chunks;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.AttributeSet;
import android.view.Menu;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EntryWidget extends LinearLayout {

    private static final StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

    @BindView(R.id.entry_check_box)
    CheckBox checkBox;

    @BindView(R.id.entry_text_view)
    TextView entryTextView;

    @BindView(R.id.entry_button_see_all_actions)
    TextView seeAllActionsButton;

    public EntryWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        super.setOrientation(HORIZONTAL);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        View.inflate(getContext(), R.layout.merge_entry, this);
        ButterKnife.bind(this);
    }

    public void bind(Day day, final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        checkBox.setOnCheckedChangeListener(null);
        checkBox.setChecked(entry.isCompleted());
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    userInteractions.onUserMarkComplete(entry);
                } else {
                    userInteractions.onUserMarkNotComplete(entry);
                }
            }
        });

        bindText(entry);
        bindMenuButton(day, entry, userInteractions);
    }

    private void bindText(Entry entry) {
        if (entry.completedTimestamp().isPresent()) {
            entryTextView.setText(entry.value(), TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) entryTextView.getText();
            spannable.setSpan(STRIKE_THROUGH_SPAN, 0, spannable.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else {
            entryTextView.setText(entry.value());
        }
    }

    private void bindMenuButton(Day day, Entry entry, ChunkEntryUserInteractions userInteractions) {
        final PopupMenu popupMenu = setupPopupMenuListener(day, entry, userInteractions);
        seeAllActionsButton.setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        popupMenu.show();
                    }

                }
        );
    }

    private PopupMenu setupPopupMenuListener(Day day, Entry entry, ChunkEntryUserInteractions userInteractions) {
        PopupMenu popupMenu = new PopupMenu(getContext(), seeAllActionsButton);
        popupMenu.inflate(R.menu.menu_entry);
        Menu menu = popupMenu.getMenu();

        if (day == Day.TODAY) {
            menu.removeItem(R.id.move_to_today);
        } else {
            menu.removeItem(R.id.move_to_tomorrow);
        }

        if (entry.isCompleted()) {
            menu.removeItem(R.id.move_to_tomorrow);
        }

        popupMenu.setOnMenuItemClickListener(new MenuClickListener(entry, userInteractions));
        return popupMenu;
    }

}

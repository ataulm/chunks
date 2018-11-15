package com.ataulm.chunks;

import android.app.Dialog;
import androidx.annotation.Nullable;
import androidx.legacy.view.ViewCompat;
import android.view.View;
import android.view.ViewGroup;

import com.novoda.accessibility.Action;
import com.novoda.accessibility.Actions;
import com.novoda.accessibility.ActionsAccessibilityDelegate;
import com.novoda.accessibility.ActionsAlertDialogCreator;

/**
 * Applies the "single-action" design pattern for the passed item view.
 * <p>
 * Removes all click/long-click listeners from view, including from children.
 * Sets the actions dialog on click, and if given, the "quick" action on long-click.
 */
public class ItemViewClickActions {

    private final View view;
    private final ActionsAlertDialogCreator dialogCreator;

    public ItemViewClickActions(View view) {
        this(view, new ActionsAlertDialogCreator(view.getContext()));
    }

    public ItemViewClickActions(View view, ActionsAlertDialogCreator dialogCreator) {
        this.view = view;
        this.dialogCreator = dialogCreator;
    }

    public void setClickListeners(Actions actions) {
        setClickListeners(actions, null);
    }

    public void setClickListeners(Actions actions, Action quickAction) {
        setClickListenersInternal(actions, quickAction);
    }

    private void setClickListenersInternal(Actions actions, @Nullable Action quickAction) {
        ActionsAccessibilityDelegate delegate = new ActionsAccessibilityDelegate(view.getResources(), actions);
        delegate.setClickLabel("see all actions");

        removeClickListenersRecursivelyFrom(view);

        Dialog dialog = dialogCreator.create(actions);

        if (actions.getCount() > 0) {
            setClickListenerToShow(dialog);
        }

        if (quickAction != null) {
            view.setOnLongClickListener(quickAction.asLongClickListener());
            delegate.setLongClickLabel(quickAction.getLabel());
        }

        ViewCompat.setAccessibilityDelegate(view, delegate);
    }

    private void removeClickListenersRecursivelyFrom(View view) {
        removeClickListenersFrom(view);
        if (view instanceof ViewGroup) {
            removeClickListenersRecursivelyFromViewGroup((ViewGroup) view);
        }
    }

    private void removeClickListenersRecursivelyFromViewGroup(ViewGroup viewGroup) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            ViewCompat.setImportantForAccessibility(child, ViewCompat.IMPORTANT_FOR_ACCESSIBILITY_NO);
            removeClickListenersRecursivelyFrom(child);
        }
    }

    private void removeClickListenersFrom(View view) {
        view.setOnLongClickListener(null);
        view.setLongClickable(false);

        view.setOnClickListener(null);
        view.setClickable(false);
    }

    private void setClickListenerToShow(final Dialog dialog) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
    }

}

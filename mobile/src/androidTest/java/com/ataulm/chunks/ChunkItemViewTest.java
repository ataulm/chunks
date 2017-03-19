package com.ataulm.chunks;

import android.support.test.espresso.ViewInteraction;

import com.novoda.espresso.ViewTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

abstract class ChunkItemViewTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    ChunkEntryUserInteractions listener;

    protected abstract ViewTestRule<ChunkItemView> getViewTestRule();

    protected ViewInteraction onItemView() {
        return onView(withClassName(is(ChunkItemView.class.getName())));
    }

    protected Entry incompleteEntry() {
        return Entry.createNew("anything");
    }

    protected Entry completeEntry() {
        // TODO: empty string shouldn't be valid timestamp (but it is)
        return completeEntry("anything");
    }

    protected Entry completeEntry(String value) {
        // TODO: empty string shouldn't be valid timestamp (but it is)
        return Entry.createFrom(Id.create(), value, "");
    }

    protected void assertDisplayingViewsWithText(int... ids) {
        for (int id : ids) {
            onView(withText(id)).check(matches(isDisplayed()));
        }
    }

    protected void bind(final Day day, final Entry entry) {
        getViewTestRule().bindViewUsing(new ViewTestRule.Binder<ChunkItemView>() {
            @Override
            public void bind(ChunkItemView view) {
                view.bind(day, entry, listener);
            }
        });
    }

}

package com.ataulm.chunks;

import android.support.test.espresso.ViewInteraction;

import com.novoda.espresso.ViewTestRule;

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

    abstract ViewTestRule<ChunkItemView> getViewTestRule();

    ViewInteraction onItemView() {
        return onView(withClassName(is(ChunkItemView.class.getName())));
    }

    Entry incompleteEntry() {
        return Entry.createNew("anything");
    }

    Entry completeEntry() {
        // TODO: empty string shouldn't be valid timestamp (but it is)
        return completeEntry("anything");
    }

    Entry completeEntry(String value) {
        // TODO: empty string shouldn't be valid timestamp (but it is)
        return Entry.createFrom(Id.create(), value, "");
    }

    void assertDisplayingViewsWithText(int... ids) {
        for (int id : ids) {
            onView(withText(id)).check(matches(isDisplayed()));
        }
    }

    void bind(final Entry entry, final ChunksActions chunksActions) {
        getViewTestRule().bindViewUsing(new ViewTestRule.Binder<ChunkItemView>() {
            @Override
            public void bind(ChunkItemView view) {
                view.bind(entry, chunksActions);
            }
        });
    }

}

package com.ataulm.chunks;

import androidx.test.espresso.ViewInteraction;

import com.novoda.espresso.ViewTestRule;

import org.junit.Rule;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

abstract class ItemViewTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    ItemUserInteractions listener;

    @Mock
    ItemViewHolder.DragStartListener dragStartListener;

    abstract ViewTestRule<ItemView> getViewTestRule();

    ViewInteraction onItemView() {
        return onView(withClassName(is(ItemView.class.getName())));
    }

    Item incompleteEntry() {
        return Item.createNew("anything");
    }

    Item completeEntry() {
        // TODO: empty string shouldn't be valid timestamp (but it is)
        return completeEntry("anything");
    }

    Item completeEntry(String value) {
        // TODO: empty string shouldn't be valid timestamp (but it is)
        return Item.createFrom(Id.create(), value, "");
    }

    void assertDisplayingViewsWithText(int... ids) {
        for (int id : ids) {
            onView(withText(id)).check(matches(isDisplayed()));
        }
    }

    void bind(final Item item, final ChunksActions chunksActions) {
        getViewTestRule().runOnMainSynchronously(new ViewTestRule.Runner<ItemView>() {
            @Override
            public void run(ItemView view) {
                ItemViewHolder viewHolder = new ItemViewHolder(view);
                viewHolder.bind(item, chunksActions, dragStartListener);
            }
        });
    }

}

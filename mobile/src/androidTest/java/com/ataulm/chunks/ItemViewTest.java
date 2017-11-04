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

abstract class ItemViewTest {

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    ItemUserInteractions listener;

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
        getViewTestRule().bindViewUsing(new ViewTestRule.Binder<ItemView>() {
            @Override
            public void bind(ItemView view) {
                ItemViewHolder viewHolder = new ItemViewHolder(view);
                viewHolder.bind(item, chunksActions);
            }
        });
    }

}

package com.ataulm.chunks;

import com.novoda.espresso.TalkBackViewTestRule;
import com.novoda.espresso.ViewTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static com.ataulm.chunks.AccessibilityViewMatchers.withUsageHintOnClick;
import static com.ataulm.chunks.ItemsFixtures.items;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemView_TalkBackEnabled_Test extends ItemViewTest {

    @Rule
    public ViewTestRule<ItemView> viewActivityRule = new TalkBackViewTestRule<>(R.layout.test_chunk_item_view);

    @Override
    protected ViewTestRule<ItemView> getViewTestRule() {
        return viewActivityRule;
    }

    @Test
    public void itemViewHasCustomUsageHint() {
        Item item = completeEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);
        bind(item, chunksActions);

        onItemView().check(matches(withUsageHintOnClick("see all actions")));
    }

    @Test
    public void givenCompleteEntryToday_click_showsMenu() {
        Item item = completeEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);
        bind(item, chunksActions);

        onItemView().perform(click());

        assertDisplayingViewsWithText(
                R.string.action_edit,
                R.string.action_move_to_tomorrow,
                R.string.action_move_to_later,
                R.string.action_delete
        );
    }

}

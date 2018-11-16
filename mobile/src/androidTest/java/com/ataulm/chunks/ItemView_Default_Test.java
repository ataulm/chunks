package com.ataulm.chunks;

import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.novoda.espresso.ViewTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.ataulm.chunks.ItemsFixtures.items;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ItemView_Default_Test extends ItemViewTest {

    @Rule
    public ViewTestRule<ItemView> viewActivityRule = new ViewTestRule<>(R.layout.test_chunk_item_view);

    @Override
    protected ViewTestRule<ItemView> getViewTestRule() {
        return viewActivityRule;
    }

    @Before
    public void setup() {
        System.out.println("");
    }

    @Test
    public void givenEntryForToday_moveRightDisplayed() {
        Item item = completeEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);

        bind(item, chunksActions);

        onView(withId(R.id.item_button_move_right)).check(matches(isDisplayed()));
    }

    @Test
    public void givenEntryForToday_moveLeftNotDisplayed() {
        Item item = completeEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);

        bind(item, chunksActions);

        onView(withId(R.id.item_button_move_left)).check(matches(not(isDisplayed())));
    }

    @Test
    public void givenEntryForTomorrow_moveLeftDisplayed() {
        Item item = completeEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TOMORROW, item, listener);

        bind(item, chunksActions);

        onView(withId(R.id.item_button_move_left)).check(matches(isDisplayed()));
    }

    @Test
    public void givenEntryForTomorrow_moveRightNotDisplayed() {
        Item item = completeEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TOMORROW, item, listener);

        bind(item, chunksActions);

        onView(withId(R.id.item_button_move_right)).check(matches(not(isDisplayed())));
    }

    @Test
    public void givenEntryForSometime_moveLeftDisplayed() {
        Item item = completeEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.SOMETIME, item, listener);

        bind(item, chunksActions);

        onView(withId(R.id.item_button_move_left)).check(matches(isDisplayed()));
    }

    @Test
    public void givenEntryForSometime_moveRightNotDisplayed() {
        Item item = completeEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.SOMETIME, item, listener);

        bind(item, chunksActions);

        onView(withId(R.id.item_button_move_right)).check(matches(not(isDisplayed())));
    }

    @Test
    public void givenEntry_correctTextDisplayed() {
        Item item = completeEntry("test");
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);

        bind(item, chunksActions);

        onView(allOf(withId(R.id.item_text_view), withText("test"))).check(matches(isDisplayed()));
    }

    @Test
    public void givenCompleteEntry_checkBoxChecked() {
        Item item = completeEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);

        bind(item, chunksActions);

        onView(withId(R.id.item_check_box)).check(matches(isChecked()));
    }

    @Test
    public void givenIncompleteEntry_checkBoxNotChecked() {
        Item item = incompleteEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);

        bind(item, chunksActions);

        onView(withId(R.id.item_check_box)).check(matches(isNotChecked()));
    }

    @Test
    public void givenCompleteEntry_clickingMenu_opensMenuWithMarkNotComplete() {
        Item item = completeEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);
        bind(item, chunksActions);

        onView(withId(R.id.item_button_menu)).perform(click());

        assertDisplayingViewsWithText(R.string.action_mark_not_complete);
    }

    @Test
    public void givenIncompleteEntry_clickingMenu_opensMenuWithMarkComplete() {
        Item item = incompleteEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);
        bind(item, chunksActions);

        onView(withId(R.id.item_button_menu)).perform(click());

        assertDisplayingViewsWithText(R.string.action_mark_complete);
    }

    @Test
    public void givenEntryForToday_clickingMenu_opensMenuWithMoveToTomorrowAndMoveToLater() {
        Item item = incompleteEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);
        bind(item, chunksActions);

        onView(withId(R.id.item_button_menu)).perform(click());

        assertDisplayingViewsWithText(
                R.string.action_move_to_tomorrow,
                R.string.action_move_to_later
        );
    }

    @Test
    public void givenEntryForTomorrow_clickingMenu_opensMenuWithMoveToTodayAndMoveToLater() {
        Item item = incompleteEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TOMORROW, item, listener);
        bind(item, chunksActions);

        onView(withId(R.id.item_button_menu)).perform(click());

        assertDisplayingViewsWithText(
                R.string.action_move_to_today,
                R.string.action_move_to_later
        );
    }

    @Test
    public void givenEntryForLater_clickingMenu_opensMenuWithMoveToTodayAndMoveToTomorrow() {
        Item item = incompleteEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.SOMETIME, item, listener);
        bind(item, chunksActions);

        onView(withId(R.id.item_button_menu)).perform(click());

        assertDisplayingViewsWithText(
                R.string.action_move_to_today,
                R.string.action_move_to_tomorrow
        );
    }

    @Test
    public void givenIncompleteEntry_clickingCheckBox_callsOnUserMarkComplete() {
        Item item = incompleteEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);
        bind(item, chunksActions);

        onView(withId(R.id.item_check_box)).perform(click());

        verify(listener).onUserMarkComplete(item);
    }

    @Test
    public void givenCompletedEntry_clickingCheckBox_callsOnUserMarkNotComplete() {
        Item item = completeEntry();
        Items items = items().with(item).get();
        ChunksActions chunksActions = ChunksActions.create(items, Day.TODAY, item, listener);
        bind(item, chunksActions);

        onView(withId(R.id.item_check_box)).perform(click());

        verify(listener).onUserMarkNotComplete(item);
    }
}

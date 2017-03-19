package com.ataulm.chunks;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.novoda.espresso.ViewTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChunkItemView_Default_Test extends ChunkItemViewTest {

    @Rule
    public ViewTestRule<ChunkItemView> viewActivityRule = new ViewTestRule<>(R.layout.test_chunk_item_view);

    @Override
    protected ViewTestRule<ChunkItemView> getViewTestRule() {
        return viewActivityRule;
    }

    @Test
    public void givenEntryForToday_moveRightDisplayed() {
        bind(Day.TODAY, completeEntry());

        onView(withId(R.id.entry_button_move_right)).check(matches(isDisplayed()));
    }

    @Test
    public void givenEntryForToday_moveLeftNotDisplayed() {
        bind(Day.TODAY, completeEntry());

        onView(withId(R.id.entry_button_move_left)).check(matches(not(isDisplayed())));
    }

    @Test
    public void givenEntryForTomorrow_moveLeftDisplayed() {
        bind(Day.TOMORROW, completeEntry());

        onView(withId(R.id.entry_button_move_left)).check(matches(isDisplayed()));
    }

    @Test
    public void givenEntryForTomorrow_moveRightNotDisplayed() {
        bind(Day.TOMORROW, completeEntry());

        onView(withId(R.id.entry_button_move_right)).check(matches(not(isDisplayed())));
    }

    @Test
    public void givenEntryForSometime_moveLeftDisplayed() {
        bind(Day.SOMETIME, completeEntry());

        onView(withId(R.id.entry_button_move_left)).check(matches(isDisplayed()));
    }

    @Test
    public void givenEntryForSometime_moveRightNotDisplayed() {
        bind(Day.SOMETIME, completeEntry());

        onView(withId(R.id.entry_button_move_right)).check(matches(not(isDisplayed())));
    }

    @Test
    public void givenEntry_correctTextDisplayed() {
        bind(Day.TODAY, completeEntry("test"));

        onView(allOf(withId(R.id.entry_text_view), withText("test"))).check(matches(isDisplayed()));
    }

    @Test
    public void givenCompleteEntry_checkBoxChecked() {
        bind(Day.TODAY, completeEntry());

        onView(withId(R.id.entry_check_box)).check(matches(isChecked()));
    }

    @Test
    public void givenIncompleteEntry_checkBoxNotChecked() {
        bind(Day.TODAY, incompleteEntry());

        onView(withId(R.id.entry_check_box)).check(matches(isNotChecked()));
    }

    @Test
    public void givenCompleteEntry_clickingMenu_opensMenuWithMarkNotComplete() {
        bind(Day.TODAY, completeEntry());

        onView(withId(R.id.entry_button_menu)).perform(click());

        assertDisplayingViewsWithText(R.string.action_mark_not_complete);
    }

    @Test
    public void givenIncompleteEntry_clickingMenu_opensMenuWithMarkComplete() {
        bind(Day.TODAY, incompleteEntry());

        onView(withId(R.id.entry_button_menu)).perform(click());

        assertDisplayingViewsWithText(R.string.action_mark_complete);
    }

    @Test
    public void givenEntryForToday_clickingMenu_opensMenuWithMoveToTomorrowAndMoveToLater() {
        bind(Day.TODAY, incompleteEntry());

        onView(withId(R.id.entry_button_menu)).perform(click());

        assertDisplayingViewsWithText(
                R.string.action_move_to_tomorrow,
                R.string.action_move_to_later
        );
    }

    @Test
    public void givenEntryForTomorrow_clickingMenu_opensMenuWithMoveToTodayAndMoveToLater() {
        bind(Day.TOMORROW, incompleteEntry());

        onView(withId(R.id.entry_button_menu)).perform(click());

        assertDisplayingViewsWithText(
                R.string.action_move_to_today,
                R.string.action_move_to_later
        );
    }

    @Test
    public void givenEntryForLater_clickingMenu_opensMenuWithMoveToTodayAndMoveToTomorrow() {
        bind(Day.SOMETIME, incompleteEntry());

        onView(withId(R.id.entry_button_menu)).perform(click());

        assertDisplayingViewsWithText(
                R.string.action_move_to_today,
                R.string.action_move_to_tomorrow
        );
    }

    @Test
    public void givenIncompleteEntry_clickingCheckBox_callsOnUserMarkComplete() {
        Entry entry = incompleteEntry();
        bind(Day.TODAY, entry);

        onView(withId(R.id.entry_check_box)).perform(click());

        verify(listener).onUserMarkComplete(entry);
    }

    @Test
    public void givenIncompleteEntry_clickingItemView_callsOnUserMarkComplete() {
        Entry entry = incompleteEntry();
        bind(Day.TODAY, entry);

        onItemView().perform(click());

        verify(listener).onUserMarkComplete(entry);
    }

    @Test
    public void givenCompletedEntry_clickingCheckBox_callsOnUserMarkNotComplete() {
        Entry entry = completeEntry();
        bind(Day.TODAY, entry);

        onView(withId(R.id.entry_check_box)).perform(click());

        verify(listener).onUserMarkNotComplete(entry);
    }

    @Test
    public void givenCompletedEntry_clickingItemView_callsOnUserMarkNotComplete() {
        Entry entry = completeEntry();
        bind(Day.TODAY, entry);

        onItemView().perform(click());

        verify(listener).onUserMarkNotComplete(entry);
    }

}

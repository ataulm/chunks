package com.ataulm.chunks;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.novoda.espresso.ViewTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.mockito.Mockito.verify;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChunkItemViewTest {

    @Rule
    public ViewTestRule<ChunkItemView> viewActivityRule = new ViewTestRule<>(R.layout.test_chunk_item_view);

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    ChunkEntryUserInteractions listener;

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
    public void givenEntryForToday_clickingMenu_opensMenuWithCorrectOptions() {
        bind(Day.TODAY, completeEntry());

        onView(withId(R.id.entry_button_menu)).perform(click());

        viewsWithTextDisplayed(
                R.string.action_edit,
                R.string.action_move_to_tomorrow,
                R.string.action_move_to_later,
                R.string.action_delete
        );
    }

    @Test
    public void givenEntryForTomorrow_clickingMenu_opensMenuWithCorrectOptions() {
        bind(Day.TOMORROW, completeEntry());

        onView(withId(R.id.entry_button_menu)).perform(click());

        viewsWithTextDisplayed(
                R.string.action_edit,
                R.string.action_move_to_today,
                R.string.action_move_to_later,
                R.string.action_delete
        );
    }

    @Test
    public void givenEntryForSometime_clickingMenu_opensMenuWithCorrectOptions() {
        bind(Day.SOMETIME, completeEntry());

        onView(withId(R.id.entry_button_menu)).perform(click());

        viewsWithTextDisplayed(
                R.string.action_edit,
                R.string.action_move_to_today,
                R.string.action_move_to_tomorrow,
                R.string.action_delete
        );
    }

    private void viewsWithTextDisplayed(int... ids) {
        for (int id : ids) {
            onView(withText(id)).check(matches(isDisplayed()));
        }
    }

    @Test
    public void givenIncompleteEntry_clickingCheckBox_hitsOnUserMarkComplete() {
        Entry entry = incompleteEntry();
        bind(Day.TODAY, entry);

        onView(withId(R.id.entry_check_box)).perform(click());

        verify(listener).onUserMarkComplete(entry);
    }

    @Test
    public void givenIncompleteEntry_clickingItemView_hitsOnUserMarkComplete() {
        Entry entry = incompleteEntry();
        bind(Day.TODAY, entry);

        onView(withClassName(is(ChunkItemView.class.getName()))).perform(click());

        verify(listener).onUserMarkComplete(entry);
    }

    @Test
    public void givenCompletedEntry_clickingCheckBox_hitsOnUserMarkNotComplete() {
        Entry entry = completeEntry();
        bind(Day.TODAY, entry);

        onView(withId(R.id.entry_check_box)).perform(click());

        verify(listener).onUserMarkNotComplete(entry);
    }

    @Test
    public void givenCompletedEntry_clickingItemView_hitsOnUserMarkNotComplete() {
        Entry entry = completeEntry();
        bind(Day.TODAY, entry);

        onView(withClassName(is(ChunkItemView.class.getName()))).perform(click());

        verify(listener).onUserMarkNotComplete(entry);
    }

    private void bind(final Day day, final Entry entry) {
        viewActivityRule.bindViewUsing(new ViewTestRule.Binder<ChunkItemView>() {
            @Override
            public void bind(ChunkItemView view) {
                view.bind(day, entry, listener);
            }
        });
    }

    private Entry incompleteEntry() {
        return Entry.createNew("anything");
    }

    private Entry completeEntry() {
        // TODO: empty string shouldn't be valid timestamp (but it is)
        return completeEntry("anything");
    }

    private Entry completeEntry(String value) {
        // TODO: empty string shouldn't be valid timestamp (but it is)
        return Entry.createFrom(Id.create(), value, "");
    }

}

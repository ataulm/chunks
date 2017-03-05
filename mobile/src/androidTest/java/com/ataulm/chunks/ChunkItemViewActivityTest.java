package com.ataulm.chunks;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChunkItemViewActivityTest {

    @Rule
    public ActivityTestRule<ChunkItemViewActivity> activityRule = new ActivityTestRule<>(ChunkItemViewActivity.class);

    private HitCounter hitCounter;

    @Before
    public void setUp() {
        hitCounter = new HitCounter(activityRule.getActivity().getResources());
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
    public void givenIncompleteEntry_clickingCheckBox_hitsOnUserMarkComplete() {
        bind(Day.TODAY, incompleteEntry());

        onView(withId(R.id.entry_check_box)).perform(click());

        hitCounter.assertHit(R.string.hits_on_user_mark_complete);
    }

    @Test
    public void givenIncompleteEntry_clickingItemView_hitsOnUserMarkComplete() {
        bind(Day.TODAY, incompleteEntry());

        onView(withId(R.id.test_view)).perform(click());

        hitCounter.assertHit(R.string.hits_on_user_mark_complete);
    }

    @Test
    public void givenCompletedEntry_clickingCheckBox_hitsOnUserMarkNotComplete() {
        bind(Day.TODAY, completeEntry());

        onView(withId(R.id.entry_check_box)).perform(click());

        hitCounter.assertHit(R.string.hits_on_user_mark_not_complete);
    }

    @Test
    public void givenCompletedEntry_clickingItemView_hitsOnUserMarkNotComplete() {
        bind(Day.TODAY, completeEntry());

        onView(withId(R.id.test_view)).perform(click());

        hitCounter.assertHit(R.string.hits_on_user_mark_not_complete);
    }

    private void bind(final Day day, final Entry entry) {
        final ChunkItemViewActivity activity = activityRule.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.chunkItemView.bind(day, entry, hitCounter);
            }
        });
    }

    private Entry incompleteEntry() {
        return Entry.createNew("anything");
    }

    private Entry completeEntry() {
        // TODO: empty string shouldn't be valid timestamp (but it is)
        return Entry.createFrom(Id.create(), "anything", "");
    }

}

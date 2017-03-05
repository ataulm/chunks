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
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChunkItemViewActivityTest {

    @Rule
    public ActivityTestRule<ChunkItemViewActivity> activityRule = new ActivityTestRule<>(ChunkItemViewActivity.class);

    private HitCounter hitCounter;

    @Before
    public void setUp() {
        hitCounter = new HitCounter();
    }

    @Test
    public void givenIncompleteEntry_clickingCheckBox_hitsOnUserMarkComplete() {
        bind(Day.TODAY, Entry.createNew("hello"), hitCounter);

        onView(withId(R.id.entry_check_box)).perform(click());

        hitCounter.assertHit(R.id.hits_on_user_mark_complete);
    }

    @Test
    public void givenIncompleteEntry_clickingItemView_hitsOnUserMarkComplete() {
        bind(Day.TODAY, Entry.createNew("hello"), hitCounter);

        onView(withId(R.id.test_view)).perform(click());

        hitCounter.assertHit(R.id.hits_on_user_mark_complete);
    }

    @Test
    public void givenCompletedEntry_clickingCheckBox_hitsOnUserMarkNotComplete() {
        bind(Day.TODAY, Entry.createFrom(Id.create(), "hello", ""), hitCounter);

        onView(withId(R.id.entry_check_box)).perform(click());

        hitCounter.assertHit(R.id.hits_on_user_mark_not_complete);
    }

    @Test
    public void givenCompletedEntry_clickingItemView_hitsOnUserMarkNotComplete() {
        bind(Day.TODAY, Entry.createFrom(Id.create(), "hello", ""), hitCounter);

        onView(withId(R.id.test_view)).perform(click());

        hitCounter.assertHit(R.id.hits_on_user_mark_not_complete);
    }

    private void bind(final Day day, final Entry entry, final ChunkEntryUserInteractions userInteractions) {
        final ChunkItemViewActivity activity = activityRule.getActivity();
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                activity.chunkItemView.bind(day, entry, userInteractions);
            }
        });
    }

}

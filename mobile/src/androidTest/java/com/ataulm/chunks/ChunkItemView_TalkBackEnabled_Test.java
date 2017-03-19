package com.ataulm.chunks;

import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.novoda.espresso.TalkBackViewTestRule;
import com.novoda.espresso.ViewTestRule;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class ChunkItemView_TalkBackEnabled_Test extends ChunkItemViewTest {

    @Rule
    public ViewTestRule<ChunkItemView> viewActivityRule = new TalkBackViewTestRule<>(R.layout.test_chunk_item_view);

    @Override
    protected ViewTestRule<ChunkItemView> getViewTestRule() {
        return viewActivityRule;
    }

    @Test
    public void givenCompleteEntryToday_click_showsMenu() {
        bind(Day.TODAY, completeEntry());

        onItemView().perform(click());

        viewsWithTextDisplayed(
                R.string.action_edit,
                R.string.action_move_to_tomorrow,
                R.string.action_move_to_later,
                R.string.action_delete
        );
    }

}

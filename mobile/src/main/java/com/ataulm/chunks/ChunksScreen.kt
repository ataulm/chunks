package com.ataulm.chunks

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.merge_chunks_screen.view.*
import java.util.*

class ChunksScreen(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs), ChunksView {

    private lateinit var tabsPagerNavigationWidget: TabsPagerNavigationWidget
    private lateinit var viewPager: ViewPager
    private lateinit var itemInputWidget: ItemInputWidget

    private val onPageChangeListenerDelegate = OnPageChangeListenerDelegate()

    override fun onFinishInflate() {
        super.onFinishInflate()
        View.inflate(context, R.layout.merge_chunks_screen, this)
        tabsPagerNavigationWidget = chunks_screen_pager_navigation_widget
        viewPager = chunks_screen_view_pager
        itemInputWidget = chunks_screen_empty_input_widget
    }

    override fun display(chunks: Chunks, itemUserInteractions: ItemUserInteractions, itemInputUserInteractions: ItemInputUserInteractions) {
        chunks.input?.let { itemInputWidget.setText(it) }

        viewPager.clearOnPageChangeListeners()
        viewPager.addOnPageChangeListener(onPageChangeListenerDelegate)
        viewPager.addOnPageChangeListener(
                object : ViewPager.SimpleOnPageChangeListener() {
                    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                        if (positionOffset != 0f) {
                            // for now, only want stuff changing when the page has settled
                            return
                        }

                        val day = DayToPagePositionMapper.getDayFor(position)
                        itemInputWidget.update(itemInputUserInteractions, day)
                    }
                }
        )

        updateViewPagerWith(chunks, itemUserInteractions)
    }

    private fun updateViewPagerWith(chunks: Chunks, itemUserInteractions: ItemUserInteractions) {
        val chunksPagerAdapter: ChunksPagerAdapter
        val dayUiModels = createDayUiModelFrom(chunks)
        if (viewPager.adapter == null) {
            chunksPagerAdapter = ChunksPagerAdapter(itemUserInteractions, viewPager.resources, dayUiModels)
            viewPager.adapter = chunksPagerAdapter
            setViewPager(Day.TODAY) // TODO: this happens on rotate, but not necessarily what we want
        } else {
            chunksPagerAdapter = viewPager.adapter as ChunksPagerAdapter
            chunksPagerAdapter.update(dayUiModels)
        }
        tabsPagerNavigationWidget.bind(viewPager)
    }

    private fun setViewPager(day: Day) {
        val page = DayToPagePositionMapper.getPageFor(day)
        viewPager.currentItem = page
    }

    private fun createDayUiModelFrom(chunks: Chunks): List<DayUiModel> {
        return Arrays.asList(
                DayUiModel(R.string.days_tabs_today, Day.TODAY, chunks.today),
                DayUiModel(R.string.days_tabs_tomorrow, Day.TOMORROW, chunks.tomorrow),
                DayUiModel(R.string.days_tabs_sometime, Day.SOMETIME, chunks.sometime)
        )
    }

}

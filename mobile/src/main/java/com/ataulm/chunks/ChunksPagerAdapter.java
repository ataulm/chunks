package com.ataulm.chunks;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.novoda.viewpageradapter.ViewPagerAdapter;

import java.util.List;

final class ChunksPagerAdapter extends ViewPagerAdapter<ChunksPage> {

    private final ItemUserInteractions userInteractions;
    private final Resources resources;

    private List<DayUiModel> dayUiModels;

    ChunksPagerAdapter(ItemUserInteractions userInteractions, Resources resources, List<DayUiModel> dayUiModels) {
        this.userInteractions = userInteractions;
        this.resources = resources;
        this.dayUiModels = dayUiModels;
    }

    void update(List<DayUiModel> dayUiModels) {
        this.dayUiModels = dayUiModels;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return dayUiModels.size();
    }

    @Override
    protected ChunksPage createView(ViewGroup container, int position) {
        LayoutInflater layoutInflater = LayoutInflater.from(container.getContext());
        return (ChunksPage) layoutInflater.inflate(R.layout.view_entries_page, container, false);
    }

    @Override
    protected void bindView(ChunksPage view, int position) {
        DayUiModel uiModel = dayUiModels.get(position);
        view.update(uiModel.items, userInteractions, uiModel.day);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return resources.getString(dayUiModels.get(position).title);
    }

}

package com.ataulm.settings;

import android.view.ViewGroup;

public class CompositeSettingsViewHolderFactory implements SettingsViewHolderFactory {

    private final SettingsViewHolderFactory customViewHolderFactory;
    private final SettingsViewHolderFactory standardViewHolderFactory = new StandardSettingsViewHolderFactory();

    CompositeSettingsViewHolderFactory() {
        this(new UnsupportedViewTypeSettingsViewHolderFactory());
    }

    CompositeSettingsViewHolderFactory(SettingsViewHolderFactory customViewHolderFactory) {
        this.customViewHolderFactory = customViewHolderFactory;
    }

    @Override
    public boolean supportsViewType(int viewType) {
        return customViewHolderFactory.supportsViewType(viewType) || standardViewHolderFactory.supportsViewType(viewType);
    }

    @Override
    public SettingsViewHolder createViewHolder(ViewGroup parent, int viewType) {
        if (customViewHolderFactory.supportsViewType(viewType)) {
            return customViewHolderFactory.createViewHolder(parent, viewType);
        }

        if (standardViewHolderFactory.supportsViewType(viewType)) {
            return standardViewHolderFactory.createViewHolder(parent, viewType);
        }

        throw new IllegalArgumentException("Unsupported viewType: " + viewType + ". Did you try passing a custom SettingsViewHolderFactory?");
    }

    private static class UnsupportedViewTypeSettingsViewHolderFactory implements SettingsViewHolderFactory {

        @Override
        public boolean supportsViewType(int viewType) {
            return false;
        }

        @Override
        public SettingsViewHolder createViewHolder(ViewGroup parent, int viewType) {
            throw new IllegalStateException("This should never be called as this implementation supports 0 viewTypes.");
        }

    }

}

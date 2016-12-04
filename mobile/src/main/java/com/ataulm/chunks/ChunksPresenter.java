package com.ataulm.chunks;

interface ChunksPresenter {

    void startPresenting();

    void stopPresenting();

    void onExternalShareText(String text);
}

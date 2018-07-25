package com.example.loanxu.demomusicplayer.interfaces;

public interface OnUpdateStatusMusic {
    void onUpdateSeekBar(int progress, int duration);

    void updateStatus(boolean isPlaying);
}

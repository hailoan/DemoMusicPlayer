package com.example.loanxu.demomusicplayer.asynctasks;

import android.content.Context;
import android.os.AsyncTask;

import com.example.loanxu.demomusicplayer.interfaces.LoadMusics;
import com.example.loanxu.demomusicplayer.models.MusicObject;
import com.example.loanxu.demomusicplayer.utils.Utils;

import java.util.List;

public class MusicAsyncTask extends AsyncTask<Void, Void, List<MusicObject>> {
    private Context mContext;
    private LoadMusics mLoadMusics;

    public MusicAsyncTask(Context mContext, LoadMusics loadMusics) {
        this.mContext = mContext;
        this.mLoadMusics = loadMusics;
    }

    @Override
    protected List<MusicObject> doInBackground(Void... voids) {
        return Utils.getAllMusic(mContext);
    }

    @Override
    protected void onPostExecute(List<MusicObject> musics) {
        mLoadMusics.loadMusics(musics);
    }
}

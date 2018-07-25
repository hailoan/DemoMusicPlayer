package com.example.loanxu.demomusicplayer.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;

import com.example.loanxu.demomusicplayer.R;
import com.example.loanxu.demomusicplayer.adapters.AdapterMusic;
import com.example.loanxu.demomusicplayer.asynctasks.MusicAsyncTask;
import com.example.loanxu.demomusicplayer.interfaces.LoadMusics;
import com.example.loanxu.demomusicplayer.interfaces.OnClickItemMusic;
import com.example.loanxu.demomusicplayer.interfaces.OnUpdateStatusMusic;
import com.example.loanxu.demomusicplayer.models.MusicObject;
import com.example.loanxu.demomusicplayer.services.MusicService;
import com.example.loanxu.demomusicplayer.utils.Contants;
import com.example.loanxu.demomusicplayer.utils.Utils;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final int PERMISSION_REQUEST = 1;
    private static final String[] PERMISSION = {Manifest.permission.READ_EXTERNAL_STORAGE};
    private SeekBar mSeekBar;
    private ImageView mImageViewPre, mImageViewPlay, mImageViewNext;
    private RecyclerView mRecyclerMusic;
    private AdapterMusic mAdapterMusic;
    private List<MusicObject> mMusics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUpView();
        checkPermiss();
    }

    public void setUpView() {
        mSeekBar = findViewById(R.id.seekbar_duration);
        mSeekBar.setOnSeekBarChangeListener(onSeekBarListener);

        mImageViewPre = findViewById(R.id.image_pre);
        mImageViewPlay = findViewById(R.id.image_play);
        mImageViewNext = findViewById(R.id.image_next);

        mImageViewNext.setOnClickListener(on_click);
        mImageViewPlay.setOnClickListener(on_click);
        mImageViewPre.setOnClickListener(on_click);
    }

    public void setUpRecyclerView() {
        mRecyclerMusic = findViewById(R.id.recycler_audio);
        mRecyclerMusic.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        mMusics = new ArrayList<>();
        mAdapterMusic = new AdapterMusic(getBaseContext(), mMusics, mOnClickItemMusic);
        mRecyclerMusic.setAdapter(mAdapterMusic);
        new MusicAsyncTask(getBaseContext(), mLoadMusic).execute();
    }

    private OnClickItemMusic mOnClickItemMusic = new OnClickItemMusic() {
        @Override
        public void onClickItemMusic(int position) {
            MusicService.activityWeakReference = new WeakReference<Activity>(MainActivity.this);
            MusicService.onUpdateStatusMusic = mOnUpdateStatusMusic;
            Intent intent = new Intent(MainActivity.this, MusicService.class);
            intent.setAction(Contants.PLAY_NEW_MUSIC);
            intent.putExtra(Contants.POSITION_MUSIC, position);
            startService(intent);
        }
    };

    private View.OnClickListener on_click = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.image_next:
                    Intent intent1 = new Intent(MainActivity.this, MusicService.class);
                    intent1.setAction(Contants.NEXT_MUSIC);
                    startService(intent1);
                    break;
                case R.id.image_play:
                    Intent intent2 = new Intent(MainActivity.this, MusicService.class);
                    intent2.setAction(Contants.PLAY_MUSIC);
                    startService(intent2);
                    break;
                case R.id.image_pre:
                    Intent intent3 = new Intent(MainActivity.this, MusicService.class);
                    intent3.setAction(Contants.PREV_MUSIC);
                    startService(intent3);
                    break;
            }
        }
    };
    private SeekBar.OnSeekBarChangeListener onSeekBarListener =
            new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (b) {

                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            };
    private OnUpdateStatusMusic mOnUpdateStatusMusic = new OnUpdateStatusMusic() {
        @Override
        public void onUpdateSeekBar(int progress, int duration) {
            mSeekBar.setMax(duration);
            mSeekBar.setProgress(progress);
        }

        @Override
        public void updateStatus(boolean isPlaying) {
            if (isPlaying) mImageViewPlay.setImageResource(R.drawable.iv_pause);
            else mImageViewPlay.setImageResource(R.drawable.iv_play);
        }
    };
    private LoadMusics mLoadMusic = new LoadMusics() {
        @Override
        public void loadMusics(List<MusicObject> musics) {
            mMusics = musics;
            mAdapterMusic.setmMusics(mMusics);
            mAdapterMusic.notifyDataSetChanged();
            MusicService.musics = mMusics;
        }
    };

    public void checkPermiss() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Utils.checkPermission(PERMISSION, MainActivity.this) == PackageManager.PERMISSION_GRANTED) {
                setUpRecyclerView();
            } else {
                MainActivity.this.requestPermissions(PERMISSION, PERMISSION_REQUEST);
            }
        } else {
            setUpRecyclerView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setUpRecyclerView();
                } else {
                    finish();
                }
                break;
            }


        }
    }
}

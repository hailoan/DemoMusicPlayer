package com.example.loanxu.demomusicplayer.services;

import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationManagerCompat;
import android.widget.Toast;

import com.example.loanxu.demomusicplayer.R;
import com.example.loanxu.demomusicplayer.interfaces.OnUpdateStatusMusic;
import com.example.loanxu.demomusicplayer.models.MusicObject;
import com.example.loanxu.demomusicplayer.utils.Contants;

import java.lang.ref.WeakReference;
import java.util.List;

public class MusicService extends Service {
    public static final int DELAY = 1000;
    private Notification mNoti;
    public static WeakReference<Activity> activityWeakReference;
    public static OnUpdateStatusMusic onUpdateStatusMusic;
    public static List<MusicObject> musics;
    private MediaPlayer mediaPlayer;
    private int mCurentMusic = -1;
    private Handler mHandler;
    private Runnable mRunnable;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case Contants.PLAY_NEW_MUSIC:
                mCurentMusic = intent.getIntExtra(Contants.POSITION_MUSIC,
                        Contants.DEFAULT_POSITION_MUSIC);
                playNewMusic(musics.get(mCurentMusic).getPath());
                mNoti = creatNotification(musics.get(mCurentMusic));
                startForeground(Contants.ID, mNoti);
                handlerLoadMusic();
                break;
            case Contants.PLAY_MUSIC:
                playMusic();
                handlerLoadMusic();
                updateNotification(musics.get(mCurentMusic));
                break;
            case Contants.NEXT_MUSIC:
                nextMusic();
                handlerLoadMusic();
                updateNotification(musics.get(mCurentMusic));
                break;
            case Contants.PREV_MUSIC:
                previousMusic();
                handlerLoadMusic();
                updateNotification(musics.get(mCurentMusic));
                break;
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public Notification creatNotification(MusicObject musicObject) {
        Intent intentNext = new Intent(this, MusicService.class);
        intentNext.setAction(Contants.NEXT_MUSIC);
        PendingIntent pdIntentNext = PendingIntent.getService(this, 0,
                intentNext, 0);
        Intent intentPre = new Intent(this, MusicService.class);
        intentPre.setAction(Contants.PREV_MUSIC);
        PendingIntent pdIntendPrev = PendingIntent.getService(this, 0,
                intentPre, 0);
        Intent intentPlay = new Intent(this, MusicService.class);
        intentPlay.setAction(Contants.PLAY_MUSIC);
        PendingIntent pdIntentPlay = PendingIntent.getService(this, 0,
                intentPlay, 0);
        Notification.Builder mNotiBuild = new Notification.Builder(this)
                .setContentTitle(musicObject.getName())
                .setContentText(musicObject.getAuthor())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setTicker(musicObject.getName())
                .addAction(R.drawable.iv_prev, getString(R.string.previous), pdIntendPrev)
                .addAction(R.drawable.iv_pause, getString(R.string.play), pdIntentPlay)
                .addAction(R.drawable.iv_next, getString(R.string.next), pdIntentNext);
        return mNotiBuild.build();
    }

    public void updateNotification(MusicObject musicObject) {
        Intent intentNext = new Intent(this, MusicService.class);
        intentNext.setAction(Contants.NEXT_MUSIC);
        PendingIntent pdIntentNext = PendingIntent.getService(this, 0,
                intentNext, 0);
        Intent intentPre = new Intent(this, MusicService.class);
        intentPre.setAction(Contants.PREV_MUSIC);
        PendingIntent pdIntendPrev = PendingIntent.getService(this, 0,
                intentPre, 0);
        Intent intentPlay = new Intent(this, MusicService.class);
        intentPlay.setAction(Contants.PLAY_MUSIC);
        PendingIntent pdIntentPlay = PendingIntent.getService(this, 0,
                intentPlay, 0);
        Notification.Builder mNotiBuild = new Notification.Builder(this)
                .setContentTitle(musicObject.getName())
                .setContentText(musicObject.getAuthor())
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setTicker(musicObject.getName())
                .addAction(R.drawable.iv_prev, getString(R.string.previous), pdIntendPrev);
        if (mediaPlayer.isPlaying()) {
            mNotiBuild.addAction(R.drawable.iv_pause, getString(R.string.pause), pdIntentPlay);
        } else {
            mNotiBuild.addAction(R.drawable.iv_play, getString(R.string.play), pdIntentPlay);
        }
        mNotiBuild.addAction(R.drawable.iv_next, getString(R.string.next), pdIntentNext);
        mNoti = mNotiBuild.build();
        NotificationManagerCompat.from(this).notify(Contants.ID, mNoti);
    }


    public void nextMusic() {
        if (mCurentMusic < musics.size()) {
            mCurentMusic++;
            playNewMusic(musics.get(mCurentMusic).getPath());
        } else {
            Toast.makeText(this, getString(R.string.end_music), Toast.LENGTH_SHORT).show();
        }
    }

    public void previousMusic() {
        if (mCurentMusic > Contants.DEFAULT_POSITION_MUSIC) {
            mCurentMusic--;
            playNewMusic(musics.get(mCurentMusic).getPath());
        } else {
            Toast.makeText(this, getString(R.string.start_music), Toast.LENGTH_SHORT).show();
        }
    }

    public void playMusic() {
        if (mediaPlayer != null) {
            playOrPauseMusic();
            onUpdateStatusMusic.updateStatus(mediaPlayer.isPlaying());
        }
    }

    public void playOrPauseMusic() {
        if (mediaPlayer.isPlaying()) mediaPlayer.pause();
        else mediaPlayer.start();
    }

    public void playNewMusic(String path) {
        stopMusic();
        mediaPlayer = MediaPlayer.create(this, Uri.parse(path));
        mediaPlayer.start();
        onUpdateStatusMusic.updateStatus(mediaPlayer.isPlaying());
    }

    public void stopMusic() {
        if (mediaPlayer != null && mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    public void handlerLoadMusic() {
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable = new Runnable() {
            @Override
            public void run() {
                if (mediaPlayer.isPlaying() && activityWeakReference.get() != null) {
                    onUpdateStatusMusic.onUpdateSeekBar(mediaPlayer.getCurrentPosition(),
                            mediaPlayer.getDuration());
                    mHandler.postDelayed(mRunnable, DELAY);
                }
            }
        }, DELAY);

    }

}

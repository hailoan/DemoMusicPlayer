package com.example.loanxu.demomusicplayer.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;

import com.example.loanxu.demomusicplayer.models.MusicObject;

import java.util.ArrayList;
import java.util.List;

public class Utils {
    public static final int COL_NAME = 0;
    public static final int COL_PATH = 1;
    public static final int COL_AUTHOR = 2;

    public static List<MusicObject> getAllMusic(Context context) {
        List<MusicObject> musics = new ArrayList<>();
        String[] projections = {MediaStore.Audio.Media.DISPLAY_NAME,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST};
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri, projections,
                null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String name, path, author;
                name = cursor.getString(COL_NAME);
                path = cursor.getString(COL_PATH);
                author = cursor.getString(COL_AUTHOR);
                musics.add(new MusicObject(name, path, author));
            }
            return musics;
        } else {
            return null;
        }
    }
    public static int checkPermission(String[] permissions, Context context) {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissions) {
            permissionCheck += ContextCompat.checkSelfPermission(context, permission);
        }
        return permissionCheck;
    }

}

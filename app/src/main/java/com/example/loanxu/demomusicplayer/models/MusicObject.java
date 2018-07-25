package com.example.loanxu.demomusicplayer.models;

import java.io.Serializable;

public class MusicObject implements Serializable {
    private String mName;
    private String mPath;
    private String mAuthor;

    public MusicObject() {
    }

    public MusicObject(String name, String path, String author) {
        this.mName = name;
        this.mPath = path;
        this.mAuthor=author;
    }

    public String getName() {
        return mName;
    }

    public String getPath() {
        return mPath;
    }

    public String getAuthor() {
        return mAuthor;
    }
}

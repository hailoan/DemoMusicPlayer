package com.example.loanxu.demomusicplayer.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.loanxu.demomusicplayer.R;
import com.example.loanxu.demomusicplayer.interfaces.OnClickItemMusic;
import com.example.loanxu.demomusicplayer.models.MusicObject;

import java.util.List;

public class AdapterMusic extends RecyclerView.Adapter<AdapterMusic.MusicHolder> {
    private Context mContext;
    private List<MusicObject> mMusics;
    private OnClickItemMusic mOnClickItemMusic;

    public AdapterMusic(Context context, List<MusicObject> musics, OnClickItemMusic onClickItemMusic) {
        this.mContext = context;
        this.mMusics = musics;
        this.mOnClickItemMusic = onClickItemMusic;
    }

    @NonNull
    @Override
    public MusicHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_music, viewGroup, false);
        return new MusicHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MusicHolder musicHolder, final int i) {
        MusicObject musicObject = getItem(i);
        musicHolder.mTitle.setText(musicObject.getName());
        musicHolder.mAuthor.setText(musicObject.getAuthor());
        musicHolder.mRelative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnClickItemMusic.onClickItemMusic(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mMusics.size();
    }

    public class MusicHolder extends RecyclerView.ViewHolder {
        private TextView mTitle, mAuthor;
        private RelativeLayout mRelative;

        public MusicHolder(@NonNull View itemView) {
            super(itemView);
            mRelative = itemView.findViewById(R.id.relative_music);
            mTitle = itemView.findViewById(R.id.text_name);
            mAuthor = itemView.findViewById(R.id.text_author);
        }
    }

    public MusicObject getItem(int position) {
        return mMusics.get(position);
    }

    public void setmMusics(List<MusicObject> musics) {
        this.mMusics = musics;
    }
}

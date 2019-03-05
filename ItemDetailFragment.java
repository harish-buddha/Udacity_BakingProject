package com.example.harish.lets_bake;

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


public class ItemDetailFragment extends Fragment {

    public static final String READY = "ready";
    public static final String Current_POSITION = "pos";
    SimpleExoPlayerView exoplayer_view;
    TextView desc_tv, shortdesc_tv;
    ImageView imageView;
    SimpleExoPlayer exoPlayer;
    long C_present;
    String videolink, description, thumbnailUrl, shortDesc;
    boolean playready = true;

    int windowindex;


    public ItemDetailFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(ItemDetailActivity.VideoUrl)) {
            videolink = getArguments().getString(ItemDetailActivity.VideoUrl);
            description = getArguments().getString(ItemDetailActivity.Description);
            thumbnailUrl = getArguments().getString(ItemDetailActivity.Thumbnail);
            shortDesc = getArguments().getString(ItemDetailActivity.Short_Description);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);
        desc_tv = rootView.findViewById(R.id.stepid);
        shortdesc_tv = rootView.findViewById(R.id.shortdesc);
        shortdesc_tv.setText(shortDesc);
        imageView = rootView.findViewById(R.id.novideoimage);
        desc_tv.setText(description);
        exoplayer_view = rootView.findViewById(R.id.exoplayer);
        if (savedInstanceState != null) {
            playready = savedInstanceState.getBoolean(READY);
            C_present = savedInstanceState.getLong(Current_POSITION);

        } else {
            if (getArguments() != null) {
                exoplayer_view = rootView.findViewById(R.id.exoplayer);


            }
        }
        return rootView;
    }

    public void initializeMediaPlayer() {
        if (exoPlayer == null) {

            exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector(), new DefaultLoadControl());
            exoplayer_view.setPlayer(exoPlayer);


            if (videolink.isEmpty()) {
                if (thumbnailUrl.isEmpty()) {
                    imageView.setVisibility(View.VISIBLE);
                    exoplayer_view.setVisibility(View.GONE);
                } else {
                    videolink = thumbnailUrl;
                    imageView.setVisibility(View.GONE);
                    exoplayer_view.setVisibility(View.VISIBLE);
                }
            }

            Uri video = Uri.parse(videolink);
            String agent = Util.getUserAgent(getActivity(), "LetsBake");
            MediaSource source = new ExtractorMediaSource(video, new DefaultDataSourceFactory(getActivity(), agent), new DefaultExtractorsFactory(), null, null);
            if (C_present != C.TIME_UNSET) {
                exoPlayer.seekTo(C_present);
            }

            exoPlayer.prepare(source);
            exoPlayer.setPlayWhenReady(playready);

        }
    }

    public void playerrelease() {
        if (exoPlayer != null) {
            updatestatepos();
            exoPlayer.stop();
            exoPlayer.release();
            exoPlayer = null;

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {

            initializeMediaPlayer();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || exoPlayer == null) {
            initializeMediaPlayer();
        }
        if (exoPlayer != null) {
            exoPlayer.setPlayWhenReady(playready);
            exoPlayer.seekTo(C_present);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (exoPlayer != null) {
            updatestatepos();
            if (Util.SDK_INT <= 23) {
                playerrelease();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (exoPlayer != null) {
            updatestatepos();
            if (Util.SDK_INT > 23) {
                playerrelease();
            }
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (exoPlayer != null) {
            updatestatepos();

            outState.putLong(Current_POSITION, C_present);
            outState.putBoolean(READY, playready);
        }

    }

    private void updatestatepos() {
        if (exoPlayer != null) {
            playready = exoPlayer.getPlayWhenReady();
            C_present = exoPlayer.getCurrentPosition();
            windowindex = exoPlayer.getCurrentWindowIndex();
        }
    }


}

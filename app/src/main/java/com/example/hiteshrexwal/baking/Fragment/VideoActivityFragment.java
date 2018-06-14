package com.example.hiteshrexwal.baking.Fragment;

import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.hiteshrexwal.baking.Extra.Step;
import com.example.hiteshrexwal.baking.R;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.TransferListener;
import com.google.android.exoplayer2.util.Util;
import java.util.Objects;

public class VideoActivityFragment extends Fragment {
    private PlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private Step step;
    private int cur_index;
    private FragmentManager manager;
    private Timeline.Window window;
    private DataSource.Factory mediaDataSourceFactory;
    private DefaultTrackSelector trackSelector;
    private boolean playWhenReady=true;
    private BandwidthMeter bandwidthMeter;
    private Context context;
    private long resumePosition=0;

    public void setStep(Step step) {
        this.step = step;
    }

    public void setManager(FragmentManager manager) {
        this.manager = manager;
    }

    public void setCur_index(int cur_index) {
        this.cur_index = cur_index;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public VideoActivityFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        //
        if (savedInstanceState != null) {
            resumePosition = savedInstanceState.getLong("pos");
            playWhenReady = savedInstanceState.getBoolean("ready");
            step=savedInstanceState.getParcelable("step");
            cur_index=savedInstanceState.getInt("index");
            Log.e("creteview","came here");
        }
        View view = inflater.inflate(R.layout.fragment_video, container, false);
        ImageView imageView=view.findViewById(R.id.fragment_image);
        playerView = view.findViewById(R.id.video_player);

            if (Objects.equals(step.getVideoURL(), "")) {
                imageView.setVisibility(View.VISIBLE);
                playerView.setVisibility(View.GONE);
                if (Objects.equals(step.getThumbnailURL(), "")) {
                    Glide.with(getContext()).load(R.drawable.novideo).into(imageView);
                } else {
                    Glide.with(getContext()).load(step.getThumbnailURL()).into(imageView);
                }

            } else {
                imageView.setVisibility(View.GONE);
                playerView.setVisibility(View.VISIBLE);
            }


        TextView heading = view.findViewById(R.id.video_heading);
        TextView details = view.findViewById(R.id.video_details);
        Button prev = view.findViewById(R.id.previous);
        Button next = view.findViewById(R.id.next);
        bandwidthMeter = new DefaultBandwidthMeter();
        mediaDataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), "mediaPlayerSample"), (TransferListener<? super DataSource>) bandwidthMeter);
        window = new Timeline.Window();

        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            heading.setText(step.getShortDescription());
            details.setText(step.getDescription());

            int last = StepsFragment.steps.size() - 1;
            if (view.findViewById(R.id.container_big) == null) {
                if (cur_index > 0 && cur_index < last) {
                    next.setVisibility(View.VISIBLE);
                    prev.setVisibility(View.VISIBLE);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            releasePlayer();
                            Step temp = StepsFragment.steps.get(cur_index + 1);
                            VideoActivityFragment fragment = new VideoActivityFragment();
                            fragment.setStep(temp);
                            fragment.setCur_index(cur_index + 1);
                            manager=getFragmentManager();
                            fragment.setManager(manager);
                            fragment.setContext(context);
                            manager.beginTransaction().replace(R.id.fragment, fragment).commit();

                        }
                    });
                    prev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            releasePlayer();
                            Step temp = StepsFragment.steps.get(cur_index - 1);
                            VideoActivityFragment fragment = new VideoActivityFragment();
                            fragment.setStep(temp);
                            fragment.setCur_index(cur_index - 1);
                            manager=getFragmentManager();
                            fragment.setManager(manager);
                            fragment.setContext(context);
                            manager.beginTransaction().replace(R.id.fragment, fragment).commit();
                        }
                    });
                } else if (cur_index == 0) {
                    next.setVisibility(View.VISIBLE);
                    next.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Step temp = StepsFragment.steps.get(cur_index + 1);
                            releasePlayer();
                            VideoActivityFragment fragment = new VideoActivityFragment();
                            fragment.setStep(temp);
                            fragment.setCur_index(cur_index + 1);
                            manager=getFragmentManager();
                            fragment.setManager(manager);
                            fragment.setContext(context);
                            manager.beginTransaction().replace(R.id.fragment, fragment).commit();
                        }
                    });
                } else if (cur_index == last) {
                    prev.setVisibility(View.VISIBLE);
                    prev.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            releasePlayer();
                            Step temp = StepsFragment.steps.get(cur_index - 1);
                            VideoActivityFragment fragment = new VideoActivityFragment();
                            fragment.setStep(temp);
                            fragment.setCur_index(cur_index - 1);
                            manager=getFragmentManager();
                            fragment.setManager(manager);
                            fragment.setContext(context);
                            manager.beginTransaction().replace(R.id.fragment, fragment).commit();
                        }
                    });
                }
            }

        }
        return view;
    }




    @Override
    public void onResume() {
        super.onResume();
            if ((Util.SDK_INT <= 23 || exoPlayer == null)) {
                if(step!=null){
                    if (Objects.equals(step.getThumbnailURL(), "")) {
                        initializePlayer(Uri.parse(step.getVideoURL()));
                    }
                }

            }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
       }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer() {
        if (exoPlayer != null) {
            resumePosition = exoPlayer.getCurrentPosition();
            playWhenReady = exoPlayer.getPlayWhenReady();
            exoPlayer.release();
            exoPlayer = null;
            trackSelector = null;
        }
    }

    private void initializePlayer(Uri uri) {
            playerView.requestFocus();

            TrackSelection.Factory videoTrackSelectionFactory =
                    new AdaptiveTrackSelection.Factory(bandwidthMeter);

            trackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

            playerView.setPlayer(exoPlayer);


            exoPlayer.setPlayWhenReady(playWhenReady);
            DefaultExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

            MediaSource mediaSource = new ExtractorMediaSource.Factory(mediaDataSourceFactory).setExtractorsFactory(extractorsFactory)
                    .createMediaSource(uri);


            //exoPlayer.prepare(mediaSource);
            boolean haveResumePosition = resumePosition != 0;
            if (haveResumePosition) {
                exoPlayer.seekTo(resumePosition);
            }
            exoPlayer.prepare(mediaSource, !haveResumePosition, false);



    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(exoPlayer!=null) {
            resumePosition = exoPlayer.getCurrentPosition();
            playWhenReady = exoPlayer.getPlayWhenReady();
        }
            outState.putLong("pos", resumePosition);
            outState.putBoolean("ready", playWhenReady);
            outState.putParcelable("step",step);
            outState.putInt("index",cur_index);
            Log.e("onstate","came here");

        super.onSaveInstanceState(outState);
    }

}

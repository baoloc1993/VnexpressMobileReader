package ngo.vnexpress.reader.views;


import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.AppCompatSeekBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import ngo.vnexpress.reader.R;

import java.text.SimpleDateFormat;
import java.util.*;

public class VideoPlayerDialog extends DialogFragment {
    private CountDownTimer countDownTimer;
    
    enum VideoState {
        STOPPED,
        PAUSED,
        PLAYING,
    }
    
    
    public static final String TAG = "VideoPlayerDialog";
    private VideoState videoState;
    private MediaController mediaController;
    public static int width;
    public static int height;
    private String videoUrl;
    private boolean isSeekBarOnDrag = false;
    private ViewGroup rootView;
    private AppCompatSeekBar progressBar;
    private ImageView imageButton;
    private VideoView videoView;
    
    public VideoPlayerDialog(String videoUrl) {
        
        this.videoUrl = videoUrl;
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.FullScreenDialog);
    }
    
    @Override
    public void onStart() {
        super.onStart();
        
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            
            progressBar = rootView.findViewById(R.id.video_player_progress);
            progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            imageButton = rootView.findViewById(R.id.video_player_play_pause_btn);
            videoView = rootView.findViewById(R.id.video_view);
            setVideoState(VideoState.STOPPED);
            progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    
                    TextView positionTextView = rootView.findViewById(R.id.video_player_current_position);
                    positionTextView.setText(miliSecondsToString(getPosition(seekBar.getProgress())));
                }
                
                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    isSeekBarOnDrag = true;
                }
                
                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    isSeekBarOnDrag = false;
                    updateVideo(seekBar.getProgress());
                    
                }
            });
            
            videoView.setOnPreparedListener(mp -> {


//                int left = videoView.getLeft();
                
                
                countDownTimer = new CountDownTimer(Long.MAX_VALUE, 200) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        getVideoProgress();
                    }
                    
                    @Override
                    public void onFinish() {
                        this.start();
                    }
                };
            });
            imageButton.setOnClickListener(v -> {
                switch (videoState) {
                    case STOPPED:
                        startVideo();
                        break;
                    case PLAYING:
                        pauseVideo();
                        break;
                    case PAUSED:
                        resumeVideo();
                        break;
                }
            });
            
            
            videoView.setOnCompletionListener(mp -> {
                countDownTimer.cancel();
                updateVideo(0);
                setVideoState(VideoState.STOPPED);
            });
            videoView.setVideoURI(Uri.parse(videoUrl));
            videoView.seekTo(100);
            
            
        }
    }
    
    private void updateVideo(int progress) {
        int position = getPosition(progress);
        
        videoView.seekTo(position);
    }
    
    private Spanned miliSecondsToString(int milisec) {
        int sec = milisec / 1000;
        int min = sec / 60;
        sec = sec % 60;
        
        
        return Html.fromHtml(String.format("<b>%d</b>:%02d", min, sec));
    }
    
    @Override
    public void onDismiss(DialogInterface dialog) {
        countDownTimer.cancel();
    }
    
    private int getPosition(int progress) {
        return (int) (videoView.getDuration() * progress / 100f);
    }
    
    private void startVideo() {
        setVideoState(VideoState.PLAYING);
        
        videoView.start();
        countDownTimer.start();
    }
    
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        
        rootView = (ViewGroup) getActivity().getLayoutInflater().inflate(R.layout.video_player_layout, container, false);
        videoView = rootView.findViewById(R.id.video_view);
        progressBar = rootView.findViewById(R.id.video_player_progress);
        imageButton = rootView.findViewById(R.id.video_player_play_pause_btn);
        Toolbar toolbar = rootView.findViewById(R.id.toolbar);
        toolbar.getNavigationIcon().mutate().setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
        toolbar.setNavigationOnClickListener(v -> this.dismiss());
        return rootView;
        
    }
    
    private void resumeVideo() {
        setVideoState(VideoState.PLAYING);
//        int currentPosition = (int) (progressBar.getProgress() * videoView.getDuration() / 100f);
//        videoView.seekTo(currentPosition);
//        videoView.resume();
        startVideo();
    }
    
    private void pauseVideo() {
        setVideoState(VideoState.PAUSED);
        imageButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
        imageButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
        
        videoView.pause();
    }
    
    private void getVideoProgress() {
        if (!isSeekBarOnDrag) {
            
            
            int currentPosition = videoView.getCurrentPosition();
            TextView positionTextView = rootView.findViewById(R.id.video_player_current_position);
            positionTextView.setText(miliSecondsToString(currentPosition));
            TextView durationTextView = rootView.findViewById(R.id.video_player_duration);
            durationTextView.setText(miliSecondsToString(videoView.getDuration()));
            int duration = videoView.getDuration();
            int progress = (int) (currentPosition * 100f / duration);
            System.out.println(progress + ":" + currentPosition + "/" + duration);
            
            progressBar.setProgress(progress);
        }
        progressBar.setSecondaryProgress(videoView.getBufferPercentage());
    }
    
    private void setVideoState(VideoState videoState) {
        this.videoState = videoState;
        switch (videoState) {
            case PAUSED:
                imageButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                imageButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                break;
            case PLAYING:
                imageButton.setImageResource(R.drawable.ic_pause_black_24dp);
                imageButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                break;
            case STOPPED:
                imageButton.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                imageButton.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                break;
        }
    }
}

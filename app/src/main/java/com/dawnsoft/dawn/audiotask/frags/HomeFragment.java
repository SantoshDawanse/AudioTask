package com.dawnsoft.dawn.audiotask.frags;


import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dawnsoft.dawn.audiotask.R;
import com.dawnsoft.dawn.audiotask.model.SimpleFragmentPagerAdapter;
import com.dawnsoft.dawn.audiotask.model.SwitchManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private Handler handler;
    private Runnable runnable;
    //switch manager class instance
    SwitchManager switchManager;

    //setting a image view as a switch
    ImageView imageSwitch, redWaves;

    AnimationDrawable animationDrawable;

    public MediaPlayer mediaPlayer;
    private AudioManager audioManager;

    //invoked when playback of a media source has completed
    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            runnable = new Runnable() {
                @Override
                public void run() {
//                    playBuzzer();
                    isChecked();
                }
            };
            handler.postDelayed(runnable, switchManager.getProgressValue());
        }
    };

    //invoked when audio focus changes
    private AudioManager.OnAudioFocusChangeListener audioFocusChangeListener = new AudioManager.OnAudioFocusChangeListener() {
        @Override
        public void onAudioFocusChange(int focusChange) {
            if(focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT ||
                    focusChange == AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK){

                mediaPlayer.pause();
                mediaPlayer.seekTo(0);
            } else if(focusChange == AudioManager.AUDIOFOCUS_GAIN){
                mediaPlayer.start();
            } else if(focusChange == AudioManager.AUDIOFOCUS_LOSS){
                releaseMediaPlayer();
            }
        }
    };

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        switchManager = new SwitchManager(getActivity().getApplicationContext());
        handler = new Handler();

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);

        redWaves = view.findViewById(R.id.fraghome_redwaves);
        redWaves.setBackgroundResource(R.drawable.animate_red_waves);
        animationDrawable = (AnimationDrawable) redWaves.getBackground();

        //image as a toggle button
        imageSwitch = view.findViewById(R.id.imageButton);
        final boolean[] imageFlag = {switchManager.isChecked()};

        //to set the proper image of toggle button
        isChecked();
        imageSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageFlag[0]){
                    imageFlag[0] = false;
                    switchManager.setChecked(imageFlag[0]);
                    isChecked();
                } else {
                    imageFlag[0] = true;
                    switchManager.setChecked(imageFlag[0]);
                    isChecked();
                }
            }
        });

        //playing the audio
        int result = audioManager.requestAudioFocus(audioFocusChangeListener, AudioManager.STREAM_MUSIC,
                AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        if (result == AudioManager.AUDIOFOCUS_REQUEST_GRANTED){
            if (switchManager.isChecked()){
//                playBuzzer();
                isChecked();
            }
        }
        return view;
    }

    @Override
    public void onStop() {
        super.onStop();
        handler.removeCallbacks(runnable);
        releaseMediaPlayer();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable);
        releaseMediaPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        mediaPlayer = MediaPlayer.create(getActivity(), R.raw.soundfile);
        if (mediaPlayer.isPlaying()){
            handler.removeCallbacks(runnable);
            releaseMediaPlayer();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isChecked();
    }

    //check whether the image button is checked or not
    private void isChecked(){
        if (switchManager.isChecked()){
            imageSwitch.setImageResource(R.drawable.orangesquito);
            playBuzzer();
            redWaves.setVisibility(View.VISIBLE);

            if (!animationDrawable.isRunning()) {
                animationDrawable.start();
            }
        } else {
            imageSwitch.setImageResource(R.drawable.bluesquito);
            animationDrawable.stop();
            redWaves.setVisibility(View.INVISIBLE);
            releaseMediaPlayer();
        }
    }

    //play buzzer continuously
    public void playBuzzer(){
        releaseMediaPlayer();
        if (switchManager.isChecked()){

            mediaPlayer = MediaPlayer.create(getActivity(), R.raw.soundfile);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(completionListener);
        }
    }

    //clean up the media player by releasing its resources
    private void releaseMediaPlayer(){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;

            //abandon the audio focus
            audioManager.abandonAudioFocus(audioFocusChangeListener);

            animationDrawable.stop();
        }
    }

}

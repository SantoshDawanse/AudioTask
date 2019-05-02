package com.dawnsoft.dawn.audiotask.frags;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.dawnsoft.dawn.audiotask.R;
import com.dawnsoft.dawn.audiotask.model.SwitchManager;


public class SettingsFragment extends Fragment {

    private SeekBar volumeBar, intensityBar;

    private AudioManager audioManager;

    private SwitchManager switchManager;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        switchManager = new SwitchManager(getActivity());

        volumeBar = view.findViewById(R.id.volumeSeekbar);
        intensityBar = view.findViewById(R.id.intensitySeekbar);

        volumeControl();
        intensityControl();
        return view;
    }

    //volume control
    private void volumeControl(){
        volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volumeBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    //playing sound in an different time interval
    private void intensityControl(){
        intensityBar.setMax(10);
        intensityBar.setProgress(switchManager.getProgressValue() / 1000);
        intensityBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int progressValue = 0;
            int delay = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
//                progressValue = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

                //setting the progress value
                /*if (progressValue <= 2.5){
                    switchManager.setProgressValue(10000);
                } else {
                    switchManager.setProgressValue((10-progressValue)*1000);
                }*/

                progressValue = seekBar.getProgress();

                if (progressValue <= 2.5){
                    delay = 10000;
                }else if (progressValue <= 5){
                    delay = 7500;
                }else if (progressValue <= 7.5){
                    delay = 5000;
                }else if (progressValue < 10){
                    delay = 2500;
                } else if (progressValue == 10){
                    delay = 0;
                }
                switchManager.setProgressValue(delay);

            }
        });
    }

}

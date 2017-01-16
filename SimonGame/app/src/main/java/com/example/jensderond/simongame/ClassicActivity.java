package com.example.jensderond.simongame;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

public class ClassicActivity extends Activity implements SoundPlayer.SoundPlayerLoadCompleteListener {
    Button _blue,_yellow,_red,_green;
    private SoundPlayer mSoundPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SoundPlayer.mContext = getApplicationContext();

        _blue           = (Button) findViewById(R.id.buttonBlue);
        _red            = (Button) findViewById(R.id.buttonRed);
        _green          = (Button) findViewById(R.id.buttonGreen);
        _yellow         = (Button) findViewById(R.id.buttonYellow);

        mSoundPlayer = new SoundPlayer();
        mSoundPlayer.setOnLoadCompleteListener(this);

        setOnTouchListeners();
    }

    public void setOnTouchListeners(){
        _blue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mSoundPlayer.playSound(SoundPlayer.BLUE_TONE);
                        _blue.setBackgroundColor( getResources().getColor(R.color.light_blue, null));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        _blue.setBackgroundColor( getResources().getColor(R.color.dark_blue, null));
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        _red.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mSoundPlayer.playSound(SoundPlayer.RED_TONE);
                        _red.setBackgroundColor( getResources().getColor(R.color.light_red, null));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        _red.setBackgroundColor( getResources().getColor(R.color.dark_red, null));
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        _green.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mSoundPlayer.playSound(SoundPlayer.GREEN_TONE);
                        _green.setBackgroundColor( getResources().getColor(R.color.light_green, null));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        _green.setBackgroundColor( getResources().getColor(R.color.dark_green, null));
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        _yellow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch(motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mSoundPlayer.playSound(SoundPlayer.YELLOW_TONE);
                        _yellow.setBackgroundColor( getResources().getColor(R.color.light_yellow, null));
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        _yellow.setBackgroundColor( getResources().getColor(R.color.dark_yellow, null));
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
    }

    @Override
    public void OnAudioLoadComplete() {
        //Do something
    }
}

package com.example.jensderond.simongame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmResults;

public class ClassicActivity extends Activity implements SoundPlayer.SoundPlayerLoadCompleteListener {
    private Button _blue,_yellow,_red,_green;
    private SoundPlayer mSoundPlayer;
    private Realm realm;
    private SharedPreferences sharedPref;
    private TextView textViewPlayerName;
    private String cur_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SoundPlayer.mContext = getApplicationContext();

        _blue               = (Button) findViewById(R.id.buttonBlue);
        _red                = (Button) findViewById(R.id.buttonRed);
        _green              = (Button) findViewById(R.id.buttonGreen);
        _yellow             = (Button) findViewById(R.id.buttonYellow);
        textViewPlayerName  = (TextView) findViewById(R.id.txtViewPlayerName);

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        cur_user = sharedPref.getString("cur_user", "");

        textViewPlayerName.setText(cur_user);


        mSoundPlayer = new SoundPlayer();
        mSoundPlayer.setOnLoadCompleteListener(this);
        Realm.init(this);
        realm = Realm.getDefaultInstance();

        RealmResults<Player> result = realm.where(Player.class).findAll();
        for(int i = 0; i < result.size(); i++){
            Log.d("Player name", result.get(i).getName());
        }

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

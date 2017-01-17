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

import com.github.zagum.switchicon.SwitchIconView;

import io.realm.Realm;
import io.realm.RealmResults;

public class ClassicActivity extends Activity implements SoundPlayer.SoundPlayerLoadCompleteListener, Score {
    public Button _blue, _yellow, _red, _green, btnStart;
    private SoundPlayer mSoundPlayer;
    private Realm realm;
    private SharedPreferences sharedPref;
    private TextView textViewPlayerName, textViewScore;
    private String cur_user, play_audio;
    private SimonSequence seq;
    private static final int YELLOW = 3, RED = 2, BLUE = 4, GREEN = 1;
    private SwitchIconView soundSwitch;
    private View btnSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    public void init() {
        SoundPlayer.mContext = getApplicationContext();

        _blue = (Button) findViewById(R.id.buttonBlue);
        _red = (Button) findViewById(R.id.buttonRed);
        _green = (Button) findViewById(R.id.buttonGreen);
        _yellow = (Button) findViewById(R.id.buttonYellow);
        btnStart = (Button) findViewById(R.id.btnStart);
        textViewPlayerName = (TextView) findViewById(R.id.txtViewPlayerName);
        textViewScore = (TextView) findViewById(R.id.txtViewRealScore);
        soundSwitch = (SwitchIconView) findViewById(R.id.switchIconSound);
        btnSound = findViewById(R.id.btnSound);

        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        cur_user = sharedPref.getString("cur_user", "");
        play_audio = sharedPref.getString("play_audio", "");
        if (play_audio.equals("")) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("play_audio", "true");
            editor.apply();
        }
        if (play_audio.equals("true")) {
            soundSwitch.setIconEnabled(true, true);
        }


        textViewPlayerName.setText(cur_user);
        displayScore(0);

        mSoundPlayer = new SoundPlayer();
        mSoundPlayer.setOnLoadCompleteListener(this);
        realm = Realm.getDefaultInstance();

        RealmResults<Player> result = realm.where(Player.class).findAll();
        for (int i = 0; i < result.size(); i++) {
            Log.d("Player name", result.get(i).getName());
        }

        setOnTouchListeners();
        seq = new SimonSequence(ClassicActivity.this);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void setLightColor(int color, boolean audio) {
        play_audio = sharedPref.getString("play_audio", "");
        if (play_audio.equals("false")) {
            audio = false;

        }
        if (color == GREEN) {
            _green.setBackgroundColor(getResources().getColor(R.color.light_green, null));
            if (audio) {
                mSoundPlayer.playSound(SoundPlayer.GREEN_TONE);
            }
            Log.d("Color", String.valueOf(color));
        } else if (color == RED) {
            _red.setBackgroundColor(getResources().getColor(R.color.light_red, null));
            if (audio) {
                mSoundPlayer.playSound(SoundPlayer.RED_TONE);
            }
            Log.d("Color", String.valueOf(color));
        } else if (color == YELLOW) {
            _yellow.setBackgroundColor(getResources().getColor(R.color.light_yellow, null));
            if (audio) {
                mSoundPlayer.playSound(SoundPlayer.YELLOW_TONE);
            }
            Log.d("Color", String.valueOf(color));
        } else if (color == BLUE) {
            _blue.setBackgroundColor(getResources().getColor(R.color.light_blue, null));
            if (audio) {
                mSoundPlayer.playSound(SoundPlayer.BLUE_TONE);
            }
            Log.d("Color", String.valueOf(color));
        }
    }

    public void setDarkColor(int color) {
        if (color == GREEN) {
            _green.setBackgroundColor(getResources().getColor(R.color.dark_green, null));
            Log.d("ColorDark", String.valueOf(color));
        }
        if (color == RED) {
            _red.setBackgroundColor(getResources().getColor(R.color.dark_red, null));
            Log.d("ColorDark", String.valueOf(color));
        }
        if (color == YELLOW) {
            _yellow.setBackgroundColor(getResources().getColor(R.color.dark_yellow, null));
            Log.d("ColorDark", String.valueOf(color));
        }
        if (color == BLUE) {
            _blue.setBackgroundColor(getResources().getColor(R.color.dark_blue, null));
            Log.d("ColorDark", String.valueOf(color));
        }
    }

    public void setOnTouchListeners() {
        _blue.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setLightColor(BLUE, true);
                        seq.sequenceHandler(BLUE);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        setDarkColor(BLUE);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        _red.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setLightColor(RED, true);
                        seq.sequenceHandler(RED);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        setDarkColor(RED);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        _green.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setLightColor(GREEN, true);
                        seq.sequenceHandler(GREEN);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        setDarkColor(GREEN);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        _yellow.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        setLightColor(YELLOW, true);
                        seq.sequenceHandler(YELLOW);
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        setDarkColor(YELLOW);
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        btnStart.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        seq.newGame();
                        return true; // if you want to handle the touch event
                }
                return false;
            }
        });
        btnSound.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent motionEvent) {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        return true; // if you want to handle the touch event
                    case MotionEvent.ACTION_UP:
                        if (soundSwitch.isIconEnabled()) {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("play_audio", "false");
                            editor.apply();
                            Log.d("Play audio", "false");
                        } else {
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("play_audio", "true");
                            editor.apply();
                            Log.d("Play audio", "true");
                        }
                        soundSwitch.switchState();
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

    @Override
    public void displayScore(int score) {
        textViewScore.setText(String.valueOf(score));
    }

    @Override
    public void saveHighscore(int score) {

        int highestScore = 0;
        try {
            highestScore = (int) (realm.where(Highscore.class).equalTo("player", cur_user).max("Score"));
        } catch (NullPointerException e) {
            Log.d("Nullpointer: ", e.getMessage());
        }
        if (highestScore < score) {
            Highscore high = new Highscore(score, cur_user);

            realm.beginTransaction();
            realm.copyToRealmOrUpdate(high);
            realm.commitTransaction();
            Log.d("Highscore saved", String.valueOf(high.getScore()));
        }
    }

    public void disableButtons() {
        _blue.setEnabled(false);
        _red.setEnabled(false);
        _yellow.setEnabled(false);
        _green.setEnabled(false);
        btnStart.setEnabled(false);
    }

    public void enableButtons() {
        _blue.setEnabled(true);
        _red.setEnabled(true);
        _yellow.setEnabled(true);
        _green.setEnabled(true);
        btnStart.setEnabled(true);
    }
}

package com.example.jensderond.simongame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends Activity implements SoundPlayer.SoundPlayerLoadCompleteListener {

    private Realm realm;
    private Button instructionsButton, aboutusButton, classicbutton, reverseButton, chooseButton;
    private SoundPlayer mSoundPlayer;
    private SharedPreferences sharedPref;
    private String cur_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init();
    }

    public void init(){
        SoundPlayer.mContext        = getApplicationContext();
        classicbutton               = (Button) findViewById(R.id.button_start);
        reverseButton               = (Button) findViewById(R.id.button_startReverse);
        instructionsButton          = (Button) findViewById(R.id.button_instructions);
        aboutusButton               = (Button) findViewById(R.id.button_about_us);
        chooseButton                = (Button) findViewById(R.id.button_select_user);
        Realm.init(this);
        realm = Realm.getDefaultInstance();
        mSoundPlayer = new SoundPlayer();
        mSoundPlayer.setOnLoadCompleteListener(this);
        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        setOnClickListeners();
    }

    public void setOnClickListeners(){
        classicbutton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mSoundPlayer.playSound(SoundPlayer.BLUE_TONE);
                cur_user = sharedPref.getString("cur_user", "");
                if ( cur_user.equals("") ) {

                    RealmResults<Player> result = realm.where(Player.class).findAll();
                    if( result.size() > 0 ) {
                        Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), NewUserActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    Intent intent = new Intent(getApplicationContext(), ClassicActivity.class);
                    startActivity(intent);
                }
            }
        });
        reverseButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                mSoundPlayer.playSound(SoundPlayer.GREEN_TONE);
                cur_user = sharedPref.getString("cur_user", "");
                if ( cur_user.equals("") ) {

                    RealmResults<Player> result = realm.where(Player.class).findAll();
                    if( result.size() > 0 ) {
                        Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Intent intent = new Intent(getApplicationContext(), NewUserActivity.class);
                        startActivity(intent);
                    }
                }
                else {
                    //start reverse game activity.
                }
            }
        });
        instructionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mSoundPlayer.playSound(SoundPlayer.RED_TONE);
                Intent intent = new Intent(getApplicationContext(), InstructionsActivity.class);
                startActivity(intent);
            }
        });
        aboutusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mSoundPlayer.playSound(SoundPlayer.YELLOW_TONE);
                Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
        chooseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mSoundPlayer.playSound(SoundPlayer.BLUE_TONE);
                Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void OnAudioLoadComplete() {
//        initPattern(); // start playing the pattern once the audio is loaded
    }

}

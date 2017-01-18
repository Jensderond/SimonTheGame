package com.example.jensderond.simongame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class MainActivity extends Activity implements SoundPlayer.SoundPlayerLoadCompleteListener {

    private Realm realm;
    private Button instructionsButton, aboutusButton, classicbutton, reverseButton, chooseButton, highscoreButton;
    private TextView versionTextView, curUsername;
    private ImageView playerProfileImage;
    private SoundPlayer mSoundPlayer;
    private SharedPreferences sharedPref;
    private String cur_user;
    private static final String VERSION = " 0.9.6";

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        init();
    }

    /**
     * onRestart
     * This is used to refresh the current player and profile image of current player.
     */
    @Override
    protected void onRestart() {
        super.onRestart();
        if (!sharedPref.getString("cur_user", "").equals("")) {
            Player result = realm.where(Player.class).equalTo("name", sharedPref.getString("cur_user", "")).findFirst();
            playerProfileImage.setImageResource(result.getImage());
            curUsername.setText(sharedPref.getString("cur_user", ""));
        }
    }

    /**
     * This function initializes stuff so the onCreate is nice and clean
     */
    public void init() {
        SoundPlayer.mContext = getApplicationContext();
        classicbutton = (Button) findViewById(R.id.button_start);
        reverseButton = (Button) findViewById(R.id.button_startReverse);
        instructionsButton = (Button) findViewById(R.id.button_instructions);
        aboutusButton = (Button) findViewById(R.id.button_about_us);
        chooseButton = (Button) findViewById(R.id.button_select_user);
        highscoreButton = (Button) findViewById(R.id.button_highscores);
        versionTextView = (TextView) findViewById(R.id.version_textView);
        playerProfileImage = (ImageView) findViewById(R.id.display_user_image);
        curUsername = (TextView) findViewById(R.id.display_user_name);
        versionTextView.setText(getApplicationContext().getString(R.string.version) + VERSION);

        /**
         * Initialize the Database
         * this can now be used in every class by calling:
         * Realm realm = Realm.getDefaultInstance();
         */
        Realm.init(this);
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);
        realm = Realm.getDefaultInstance();
        mSoundPlayer = new SoundPlayer();
        mSoundPlayer.setOnLoadCompleteListener(this);
        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        curUsername.setText(sharedPref.getString("cur_user", ""));

        if (!sharedPref.getString("cur_user", "").equals("")) {
            Player result = realm.where(Player.class).equalTo("name", sharedPref.getString("cur_user", "")).findFirst();

            playerProfileImage.setImageResource(result.getImage());
        }

        setOnClickListeners();
    }

    /**
     * This function holds all the setOnClickListeners methods
     * of all the menu buttons
     */
    public void setOnClickListeners() {
        classicbutton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cur_user = sharedPref.getString("cur_user", "");
                if (cur_user.equals("")) {

                    RealmResults<Player> result = realm.where(Player.class).findAll();
                    if (result.size() > 0) {
                        Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), NewUserActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), ClassicActivity.class);
                    intent.putExtra("GameMode", "Classic");
                    startActivity(intent);
                }
            }
        });
        reverseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                cur_user = sharedPref.getString("cur_user", "");
                if (cur_user.equals("")) {

                    RealmResults<Player> result = realm.where(Player.class).findAll();
                    if (result.size() > 0) {
                        Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(getApplicationContext(), NewUserActivity.class);
                        startActivity(intent);
                    }
                } else {
                    Intent intent = new Intent(getApplicationContext(), ClassicActivity.class);
                    intent.putExtra("GameMode", "Twisted");
                    startActivity(intent);
                }
            }
        });
        instructionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), InstructionsActivity.class);
                startActivity(intent);
            }
        });
        aboutusButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
        chooseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UsersActivity.class);
                startActivity(intent);
            }
        });
        highscoreButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), HighscoreActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * This must be implemented for the SoundPlayer to call back that the audio files are loaded.
     */
    @Override
    public void OnAudioLoadComplete() {
//        initPattern(); // start playing the pattern once the audio is loaded
    }

}

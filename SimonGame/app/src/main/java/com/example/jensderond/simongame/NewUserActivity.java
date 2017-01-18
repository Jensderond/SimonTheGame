package com.example.jensderond.simongame;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ResourceCursorAdapter;
import android.widget.Toast;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by jensderond on 16/01/2017.
 */

public class NewUserActivity extends Activity {

    private Realm realm;
    private Button addUser;
    private EditText username;
    private RadioButton checked, male, female;
    private RadioGroup radioGroup;
    private SharedPreferences sharedPref;

    /**
     * onCreate
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        addUser = (Button) findViewById(R.id.btn_add_user);
        username = (EditText) findViewById(R.id.txt_username);
        male = (RadioButton) findViewById(R.id.radio_male);
        female = (RadioButton) findViewById(R.id.radio_female);
        radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        male.setChecked(true);
        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        realm = Realm.getDefaultInstance();


        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedID = radioGroup.getCheckedRadioButtonId();
                checked = (RadioButton) findViewById(selectedID);

/**
 * get all the users and create new one if needed
 */
                RealmResults<Player> result = realm.where(Player.class).equalTo("name", username.getText().toString()).findAll();
                if (result.isEmpty() && !username.equals("") && checked.isChecked()) {

                    Player player = new Player();
                    player.setName(username.getText().toString());
                    if (checked.getText().toString().equals("Man")){
                        player.setGender("male");
                    }
                    if (checked.getText().toString().equals("Vrouw")){
                        player.setGender("female");
                    }
                    if (checked.getText().toString().equals("Anders")){
                        player.setGender("other");
                    }
/**
 * transaction to the database to update a player
 */
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(player);
                    realm.commitTransaction();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("cur_user", player.getName());
                    editor.apply();

                    Toast.makeText(NewUserActivity.this,
                            "Gebruiker: " + player.getName() + " toegevoegd!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else if(!result.isEmpty()){
                    Toast.makeText(NewUserActivity.this,
                            R.string.error_fill_username_taken, Toast.LENGTH_SHORT).show();
                }
                else if( username.equals("")){
                    Toast.makeText(NewUserActivity.this,
                            R.string.error_fill_username, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

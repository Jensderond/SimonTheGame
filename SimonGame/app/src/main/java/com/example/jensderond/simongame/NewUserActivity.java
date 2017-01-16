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

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import io.realm.Realm;

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


        Realm.init(this);
        realm = Realm.getDefaultInstance();


        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int selectedID = radioGroup.getCheckedRadioButtonId();
                checked = (RadioButton) findViewById(selectedID);

                if (!username.equals("") && checked.isChecked()) {

                    realm.beginTransaction();
                    int nextID = 1;

                    try {
                        nextID = (int) (realm.where(Player.class).max("id").longValue() + 1);
                    } catch (NullPointerException e){
                        Log.d("Nullpointer: ",e.getMessage());
                    }
                    Player player = realm.createObject(Player.class);
                    player.setID(nextID);
                    player.setName(username.getText().toString());
                    player.setGender(checked.getText().toString());

                    realm.commitTransaction();

                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.putString("cur_user", player.getName());
                    editor.apply();

                    Toast.makeText(NewUserActivity.this,
                            "Gebruiker: " + player.getName() + " toegevoegd!", Toast.LENGTH_SHORT).show();
                    finish();
                }
                else {
                    Toast.makeText(NewUserActivity.this,
                            R.string.error_fill_username, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}

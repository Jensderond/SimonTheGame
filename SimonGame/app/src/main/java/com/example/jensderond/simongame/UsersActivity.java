package com.example.jensderond.simongame;

import android.app.Activity;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by jensderond on 16/01/2017.
 */

public class UsersActivity extends Activity implements AdapterView.OnItemClickListener {
    private Realm realm;
    private Button buttonNewUser;
    private ArrayList<String> arrayListUsers = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ListView lvUsers;
    private SharedPreferences sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        init();
    }

    public void init(){
        buttonNewUser       = (Button)          findViewById(R.id.button_newUser);
        lvUsers             = (ListView)        findViewById(R.id.lvUsers);
        lvUsers.setOnItemClickListener(this);
        sharedPref = getSharedPreferences("userInfo", Context.MODE_PRIVATE);

        buttonNewUser.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), NewUserActivity.class);
                startActivity(intent);
            }
        });

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrayListUsers );

        realm = Realm.getDefaultInstance();

        RealmResults<Player> result = realm.where(Player.class).findAll();
        if ( result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Log.d("Player name", result.get(i).getName());
                arrayListUsers.add(result.get(i).getName());
            }
        }
        lvUsers.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onStart() {
        super.onStart();

        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();

        arrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        String username = ((TextView) view).getText().toString();

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("cur_user", username);
        editor.apply();
        Log.d("User Clicked", username);

        Toast.makeText(UsersActivity.this,
                "Gebruiker: " + username + " geselecteerd!", Toast.LENGTH_SHORT).show();
    }


}

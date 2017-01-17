package com.example.jensderond.simongame;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by jensderond on 17/01/2017.
 */

public class HigscoreActivity extends Activity {
    private Realm realm;
    private ArrayList<String> arrayListHighscores = new ArrayList<>();
    private ArrayAdapter<String> arrayAdapter;
    private ListView lvHighscores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        init();
    }

    public void init(){
        lvHighscores        = (ListView)        findViewById(R.id.highscoresListView);

        arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                arrayListHighscores );

        realm = Realm.getDefaultInstance();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshData();
    }

    public void refreshData(){

        RealmResults<Highscore> result = realm.where(Highscore.class).findAll();
        arrayListHighscores.clear();
        if ( result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Log.d("Player name", result.get(i).getPlayer());
                Log.d("Highscore", String.valueOf(result.get(i).getScore()));
                arrayListHighscores.add(result.get(i).getPlayer());
            }
        }
        lvHighscores.setAdapter(arrayAdapter);
        arrayAdapter.notifyDataSetChanged();
    }
}

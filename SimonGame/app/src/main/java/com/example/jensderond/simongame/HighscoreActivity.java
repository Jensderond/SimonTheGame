package com.example.jensderond.simongame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by jensderond on 17/01/2017.
 */

public class HighscoreActivity extends Activity {
    private Realm realm;
    private ArrayList<Highscore> arrayListHighscores = new ArrayList<>();
    private HighscoreListAdapter highscoreListAdapter;
    private ListView lvHighscores;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        init();
    }

    public void init(){
        lvHighscores        = (ListView)        findViewById(R.id.highscoresListView);
        realm = Realm.getDefaultInstance();
        refreshData();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public void refreshDataView(ArrayList<Highscore> highscores) {

        highscoreListAdapter = new HighscoreListAdapter(this, highscores);
        lvHighscores.setAdapter(highscoreListAdapter);
        highscoreListAdapter.notifyDataSetChanged();
    }

    public void refreshData(){

        RealmResults<Highscore> result = realm.where(Highscore.class).findAll().sort("score", Sort.DESCENDING);
        arrayListHighscores.clear();
        if ( result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Log.d("Player name", result.get(i).getPlayer());
                Log.d("Highscore", String.valueOf(result.get(i).getScore()));
                arrayListHighscores.add(result.get(i));
            }
        }
        refreshDataView(arrayListHighscores);
    }
}

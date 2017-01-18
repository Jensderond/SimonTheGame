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

    /**
     * onCreate
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscores);
        init();
    }

    /**
     * This function initializes stuff so the onCreate is nice and clean
     */
    public void init() {
        lvHighscores = (ListView) findViewById(R.id.highscoresListView);
        realm = Realm.getDefaultInstance();
        refreshData();
    }

    /**
     * onResume
     */
    @Override
    protected void onResume() {
        super.onResume();

    }

    /**
     * this function refreshes the data of the current highscorelist in the view
     *
     * @param highscores
     */
    public void refreshDataView(ArrayList<Highscore> highscores) {

        highscoreListAdapter = new HighscoreListAdapter(this, highscores);
        lvHighscores.setAdapter(highscoreListAdapter);
        highscoreListAdapter.notifyDataSetChanged();
    }

    /**
     * this function refreshes the players highscore in the database
     */
    public void refreshData() {

        RealmResults<Highscore> result = realm.where(Highscore.class).findAll().sort("score", Sort.DESCENDING);
        arrayListHighscores.clear();
        if (result != null && result.size() > 0) {
            for (int i = 0; i < result.size(); i++) {
                Log.d("Player name", result.get(i).getPlayer());
                Log.d("Highscore", String.valueOf(result.get(i).getScore()));
                arrayListHighscores.add(result.get(i));
            }
        }
        refreshDataView(arrayListHighscores);
    }
}

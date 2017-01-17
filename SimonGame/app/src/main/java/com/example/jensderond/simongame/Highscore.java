package com.example.jensderond.simongame;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by jensderond on 17/01/2017.
 */

public class Highscore extends RealmObject {
    @PrimaryKey @Required
    private String player;
    private int score;

    public Highscore(){}

    public Highscore(int score, String player){
        setScore(score);
        setPlayer(player);
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getPlayer() {
        return player;
    }

    public void setPlayer(String player) {
        this.player = player;
    }
}

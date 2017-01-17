package com.example.jensderond.simongame;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;

/**
 * Created by ruben on 16-1-2017.
 */

public class SimonSequence extends AsyncTask<Void, Void, Void> implements IState {

    private ClassicActivity cd;
    private long mPatternStepStartTimeMillis = 0;
    private int mCurrentPatternStep = 0;
    private ArrayList<Integer> sequence = new ArrayList<>();
    private int seqLevel = 1;
    private int seqCount = 1;
    private int key = -1;
    private int score = 0;
    private int count = 0;
    private State state;
    private CountDownTimer timer = null;

    public SimonSequence(ClassicActivity context) {
        this.cd = context;
        stateIdle();
        checkState();
    }

    // Nieuwe game starten
    public void newGame(int seqLevel) {
        this.seqLevel = seqLevel;
        //nieuwe sequence maken

        //state van idle naar start zetten
        if (state == State.IDLE) {
            stateStart();
            Log.d("State", "Start new game");
            checkState();
        }
    }

    public void newGame() {
        //state van idle naar start zetten
        if (state == State.IDLE) {
            stateStart();
            Log.d("State", "Start new game");
            checkState();
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkState();
            }
        }, 750);
        return null;
    }

    //checken states
    private void checkState() {
        switch (state) {
            //wanneer je IDLE bent
            case IDLE:
                Log.d("state", "IDLE");
                break;
            //spel starten
            case START:
                Log.d("state", "START");
                sequence.clear();
                //nieuwe sequence random in laten laden
                for (int i = 0; i < seqLevel; i++) {
                    addToSequence();
                }
                //level en counter op 0 zetten #new game
                seqCount = 1;
                for (int i = 0; i < sequence.size(); i++) {
                    Log.d("", String.valueOf(sequence.get(i)));
                }

                //Laat score zien in activity
                cd.displayScore(seqLevel - 1);
                stateShow();
                checkState();
                break;

            //speler de nieuwe seq laten zien
            case SHOWSEQ:
                if (count == 0) {
                    timer = new CountDownTimer(1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            Log.d("nog ", String.valueOf(millisUntilFinished));
                        }

                        public void onFinish() {
                            stateShow();
                            count++;
                            checkState();
                        }
                    }.start();
                }
                else {
                    Log.d("state", "SHOW");
                    try {
                        // The pattern step just started, turn the light on

                        if (mPatternStepStartTimeMillis == 0 && seqCount - 1 < sequence.size()) {
                            mPatternStepStartTimeMillis = System.currentTimeMillis();

                            int lightQuadrant = sequence.get(seqCount - 1);

                            cd.setLightColor(lightQuadrant, true);

                            doInBackground();
                        } else if ((System.currentTimeMillis() - mPatternStepStartTimeMillis) >= 750) {
                            // turn the light off
                            cd.setDarkColor(sequence.get(seqCount - 1));
                            mPatternStepStartTimeMillis = 0;
                            // if we just completed the last step in the pattern, reset to simply drawing the board
                            seqCount++;
                            if (seqCount - 1 == sequence.size()) {
                                seqCount = 1;
                                count = 0;
                                stateIdle();
                                checkState();
                            }
                            checkState();
                        }

                    } catch (IndexOutOfBoundsException ex) {
                        Log.d("Index out of bounds", ex.getMessage());
                    }
                }
                break;
            //player moet de sequence input geven
            case PLAYSEQ:
                Log.d("state", "PLAY");
//                hier een leuke timer die 5 seconden telt speel je niet ben je af.
                break;

            // LOSING STATE
            case LOST:
                Log.d("state", "LOST");
                Toast.makeText(cd,
                        "Helaas verloren!", Toast.LENGTH_SHORT).show();
                stateIdle();
                checkState();
                break;
            // WINNER STATE
            case WINNER:
                Log.d("state", "WINNER");
                new CountDownTimer(2500, 500) {
                    public void onTick(long millisecCounter) {
                    }

                    public void onFinish() {
                        stateIdle();
                        checkState();
                    }
                }.start();
                break;
            case PLAYING:
                Log.d("state", "Playing");
                if (key != sequence.get(seqCount -1)) {
                    cd.saveHighscore(getScore() - 1);
                    stateLost();
                    seqLevel = 1;
                    seqCount = 1;
                    checkState();
                } else {
                    if (seqCount == seqLevel) {
                        if (seqCount == 50) {
                            stateWinner();
                        } else {
                            //naar het volgende level
                            seqLevel++;
                            seqCount = 1;
                            addToSequence();
                            cd.displayScore(seqLevel - 1);
                            stateShow();
                        }
                        checkState();
                    } else {
                        seqCount++;
                        statePlay();
                        checkState();
                    }
                }
                break;
        }
    }

    private void addToSequence(){
        Random getal = new Random();
        sequence.add((getal.nextInt(4) + 1));
    }

    @Override
    public void stateIdle() {
        state = State.IDLE;
    }

    @Override
    public void stateStart() {
        state = State.START;
    }

    @Override
    public void stateShow() {
        state = State.SHOWSEQ;
    }

    public void statePlay() {
        state = State.PLAYSEQ;
    }

    @Override
    public void stateLost() {
        state = State.LOST;
    }

    @Override
    public void stateWinner() {
        state = State.WINNER;
    }

    @Override
    public void statePlaying() {
        state = State.PLAYING;
    }

    @Override
    public void sequenceHandler(int key) {

        if (!sequence.isEmpty()) {
            this.key = key;
            statePlaying();
            checkState();
        }
    }

    @Override
    public int getScore(){
        this.score = seqLevel;
        return score;
    }

    @Override
    public State getState(){
        return state;
    }
}


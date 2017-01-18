package com.example.jensderond.simongame;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
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
    private CountDownTimer timeouttimer = null;
    private boolean isrunning = false;
    private State state;
    private GameMode gameMode;
    private CountDownTimer timer = null;

    public SimonSequence(ClassicActivity context) {
        this.cd = context;
        stateIdle();
        checkState();
    }

    // Nieuwe game starten
    public void newGame(int seqLevel, GameMode gameMode) {
        if (gameMode == GameMode.CLASSIC){
            this.gameMode = GameMode.CLASSIC;
        }
        if (gameMode == GameMode.TWISTED){
            this.gameMode = GameMode.TWISTED;
        }
        this.seqLevel = seqLevel;
        //nieuwe sequence maken

        //state van idle naar start zetten
        if (state == State.IDLE) {
            stateStart();
            Log.d("State", "Start new game");
            checkState();
        }
    }

    public void newGame(GameMode gameMode) {
        if (gameMode == GameMode.CLASSIC){
            this.gameMode = GameMode.CLASSIC;
        }
        if (gameMode == GameMode.TWISTED){
            this.gameMode = GameMode.TWISTED;
        }
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
                //level op 1 en counter op 0 zetten #new game
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
                cd.disableButtons();
                if (count == 0) {
                    timer = new CountDownTimer(1000, 1000) {
                        public void onTick(long millisUntilFinished) {
                            setAllDark();
                            Log.d("nog ", String.valueOf(millisUntilFinished));
                        }

                        public void onFinish() {
                            stateShow();
                            count++;
                            checkState();
                        }
                    }.start();
                } else {
                    Log.d("state", "SHOW");
                    try {
                        // The pattern step just started, turn the light on
                       setAllDark();
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
                                setAllDark();
                                statePlay();
                                if (gameMode == GameMode.TWISTED) {
                                    Collections.reverse(sequence);
                                }
                                cd.enableButtons();
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
                startTimeout(5000);

                break;

            // LOSING STATE
            case LOST:
                Log.d("state", "LOST");
                Toast.makeText(cd,
                        "Helaas verloren!", Toast.LENGTH_SHORT).show();
                cd.saveHighscore(getScore() - 1);
                seqLevel = 1;
                seqCount = 1;
                sequence.clear();
                score = 0;
                cd.displayScore(score);
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
                if (key != sequence.get(seqCount - 1)) {
                    stateLost();
                    checkState();
                } else {
                    if (seqCount == seqLevel) {
                        if (seqCount == 50) {
                            stateWinner();
                        } else {
                            //naar het volgende level
                            seqLevel++;
                            seqCount = 1;
                            if(gameMode == GameMode.TWISTED) {
                                Collections.reverse(sequence);
                            }
                            addToSequence();
                            cd.displayScore(getScore());
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

    private void addToSequence() {
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

        if (!sequence.isEmpty() && state == State.PLAYSEQ) {
            this.key = key;
            stopTimeout();
            statePlaying();
            checkState();
        }
    }

    @Override
    public int getScore() {
        this.score = seqLevel - 1;
        return score;
    }

    @Override
    public State getState() {
        return state;
    }

    private void startTimeout(int timeout) {

        timeouttimer = new CountDownTimer(timeout, 500) {
            public void onTick(long millisUntilFinished) {
                isrunning = true;
            }

            public void onFinish() {
                isrunning = false;
                stateLost();
                checkState();
            }
        }.start();
    }

    private void stopTimeout() {

        if (isrunning && timeouttimer != null) {
            timeouttimer.cancel();
            timeouttimer = null;
            isrunning = false;
        }
    }

    private void setAllDark() {
        for (int i = 1; i < 5; i++) {
            cd.setDarkColor(i);
        }
    }

    public void destroyGame(){
        cd.saveHighscore(getScore());
        seqLevel = 1;
        seqCount = 1;
        sequence.clear();
        score = 0;
        stopTimeout();
        Log.d("Game destroyed","clear");
        stateIdle();
        checkState();
    }
}



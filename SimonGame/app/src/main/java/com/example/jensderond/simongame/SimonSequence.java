package com.example.jensderond.simongame;

import android.content.Context;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.view.View;

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
    //sequence aanmaken
    private int[] sequence;
    private int seqLevel = 0;
    private int seqCount = 0;
    private int key = -1;
    //instantieren timer
    private CountDownTimer timer = null;

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
    public void sequenceHandler(int key) {

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

    //aanmaken verschillede gamestates

    private enum State {
        IDLE, START, PLAYSEQ, LOST, WINNER, SHOWSEQ
    }

    //setten default gamestate
    private State state = State.IDLE;

    public SimonSequence(ClassicActivity context) {
        this.cd = context;
        checkState();
    }

    // Nieuwe game starten
    public void newGame(int seqLevel) {
        //nieuwe sequence maken
        sequence = new int[seqLevel];

        //state van idle naar start zetten
        if (state == State.IDLE) {
            stateStart();
            Log.d("NEW GAME", "NEWWWW GAMMEEEEEEEEE");
            checkState();
        }
    }

    //
    //  Async input triggers
    //
    public void key(int key) {
        if (state == State.PLAYSEQ) {
            this.key = key;
            stopTimeout();
            checkState();

        }
    }

    private void startTimeout(int timeout) {

        timer = new CountDownTimer(timeout, 500) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                state = State.LOST;
                checkState();
            }
        }.start();
    }

    private void stopTimeout() {

        if (timer != null) {
            timer.cancel();
            timer = null;
        }
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
                //nieuwe sequence random in laten laden
                for (int i = 0; i < sequence.length; i++) {
                    Random getal = new Random();
                    sequence[i] = (getal.nextInt(4) + 1);
                }
                //level en counter op 0 zetten #new game
                seqCount = 0;
                seqLevel = 0;
                for (int i = 0; i < sequence.length; i++) {
                    Log.d("", String.valueOf(sequence[i]));
                }
                //sequence laten zien in het nieuwe spel
                stateShow();
                checkState();
                break;

            //speler de nieuwe seq laten zien
            case SHOWSEQ:
                Log.d("state", "SHOW");
                try {
                    // The pattern step just started, turn the light on

                    if (mPatternStepStartTimeMillis == 0 && seqCount < sequence.length) {
                        mPatternStepStartTimeMillis = System.currentTimeMillis();

                        int lightQuadrant = sequence[seqCount];

                        cd.setLightColor(lightQuadrant);
                        checkState();
                    } else if ((System.currentTimeMillis() - mPatternStepStartTimeMillis) >= 750) {
                        // turn the light off
                        cd.setDarkColor(sequence[seqCount]);
                        mPatternStepStartTimeMillis = 0;
                        // if we just completed the last step in the pattern, reset to simply drawing the board
                        seqCount++;
                        if (seqCount == sequence.length) {
                            seqCount = 0;
                            statePlay();
                            checkState();
                        }
                        checkState();
                    }

                } catch (IndexOutOfBoundsException ex) {
                    Log.d("HOEMOES", "HOEMOES");
                }
                doInBackground();
//                if (seqCount <= seqLevel) {
//                    sequenceHandler(sequence[seqCount]);
//

                //delay in de show van je sequence
//                    for(int i = 0; i < sequence.length; i++) {
//                        new CountDownTimer(2500, 2500/sequence.length) {
//                            public void onTick(long millisUntilFinished) {
//                                //        String lol = "yee";
//                                cd.setLightColor(sequence[seqCount]);
//                            }
//
//                            public void onFinish() {
//                                //                String abc = "abc";
//                                cd.setDarkColor(sequence[seqCount]);
//                                checkState();
//                            }
//                        }.start();
//                    }
//                    seqCount++;
//                } else {
//
//                    seqCount = 0;
//                    state = State.PLAYSEQ;
//                    //timeout wannneer het misgaat
//                    startTimeout(5000);
//                }
                break;
            //player moet de sequence input geven
            case PLAYSEQ:
                Log.d("state", "PLAY");
                if (key != sequence[seqCount]) {
                   stateLost();
                    checkState();
                } else {
                    if (seqCount == seqLevel) {
                        if (seqCount == sequence.length - 1) {
                            stateWinner();
                        } else {
                            //naar het volgende level
                            stateShow();
                            seqLevel++;
                            seqCount = 0;
                        }
                        checkState();
                    } else {
                        sequenceHandler(key);
                        seqCount++;
                        startTimeout(5000);
                        state = State.PLAYSEQ;
                    }
                }
                break;

            // LOSING STATE
            case LOST:
                Log.d("state", "LOST");
                new CountDownTimer(2500, 500) {
                    public void onTick(long millisecCounter) {
                    }

                    public void onFinish() {
                        stateIdle();
                        checkState();
                    }
                }.start();
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
            default:
        }
    }
}


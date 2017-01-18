package com.example.jensderond.simongame;

import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

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
    private int timeout = 450;
    private int lastcolor = 0;
    private CountDownTimer timeouttimer = null;
    private boolean isrunning = false;
    private boolean attracting = false;
    private State state;
    private GameMode gameMode;
    private CountDownTimer timer = null;

    /**
     * constructor
     *
     * @param context
     */
    public SimonSequence(ClassicActivity context) {
        this.cd = context;
        stateIdle();
        checkState();
    }

    /**
     * function for starting a newGame with the check for the appropriate gamemode
     *
     * @param gameMode
     */

    public void newGame(GameMode gameMode) {
        if (gameMode == GameMode.CLASSIC) {
            this.gameMode = GameMode.CLASSIC;
        }
        if (gameMode == GameMode.TWISTED) {
            this.gameMode = GameMode.TWISTED;
        }
        //state van idle naar start zetten
        if (state == State.IDLE) {
            stateStart();
            Log.d("State", "Start new game");
            checkState();
        }
    }

    /**
     * this function checks what the current state of the game is and calls the funtions accordingly
     */
    private void checkState() {
        switch (state) {
            case IDLE:
                /**
                 * this is the case where a timer starts when you a idle. if youre idle for to long the game will go to attract mode.
                 */
                if (!attracting) {
                    Log.d("state", "IDLE");
                    timeouttimer = new CountDownTimer(5000, 500) {
                        public void onTick(long millisUntilFinished) {
                            isrunning = true;
                            Log.d("IDLE TIMER ", String.valueOf(millisUntilFinished));
                        }

                        public void onFinish() {
                            attracting = true;
                            timeout = 450;
                            for (int i = 0; i < 100; i++) {
                                addToSequence();
                            }
                            checkState();
                        }
                    }.start();
                } else if (attracting) {
                    try {
                        // The pattern step just started, turn the light on
                        if (mPatternStepStartTimeMillis == 0) {
                            mPatternStepStartTimeMillis = System.currentTimeMillis();

                            int color = sequence.get(seqCount - 1);
                            if (lastcolor == 0) {
                                lastcolor = color;
                            } else if (color == lastcolor && color >= 3) {
                                color--;
                            } else if (color == lastcolor && color <= 2) {
                                color++;
                            }

                            int lightQuadrant = color;

                            cd.setLightColor(lightQuadrant, true);

                            lastcolor = color;
                            /**
                             * callbacktimer
                             */
                            doInBackground();
                        } else if ((System.currentTimeMillis() - mPatternStepStartTimeMillis) >= timeout) {
                            cd.setDarkColor(lastcolor);
                            mPatternStepStartTimeMillis = 0;
                            seqCount++;
                            if (seqCount - 1 == sequence.size()) {
                                seqCount = 1;
                                count = 0;
                                setAllDark();
                                attracting = false;
                            }
                            checkState();
                        }

                    } catch (IndexOutOfBoundsException ex) {
                        Log.d("Index out of bounds", ex.getMessage());
                    }
                }
                break;
            /**
             * case when you start a game, get a new sequence.
             */
            case START:
                Log.d("state", "START");
                resetAllVariables();
                for (int i = 0; i < seqLevel; i++) {
                    addToSequence();
                }
                timeout = 1500;

                /**
                 * display current score in your activity
                 */
                cd.displayScore(seqLevel - 1);
                stateShow();
                checkState();
                break;

            /**
             * function where the player is shown the sequence which they have to copy
             */
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
                            /**
                             * callbackfunction
                             */
                            doInBackground();
                        } else if ((System.currentTimeMillis() - mPatternStepStartTimeMillis) >= timeout) {
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
            /**
             * in this case the player has just seen the sequence and has to copy the sequence. A timer starts to see if the player is idle
             */
            case PLAYSEQ:
                Log.d("state", "PLAY");
                startTimeout(5000);

                break;

            /**
             * You get to this case if you lose. your sequence will be reset
             */
            case LOST:
                Log.d("state", "LOST");
                Toast.makeText(cd,
                        "Helaas verloren!", Toast.LENGTH_SHORT).show();
                cd.saveHighscore(getScore() - 1);
                resetAllVariables();
                cd.displayScore(score);
                stateIdle();
                checkState();
                break;
            /**
             * You get here when you win (level 50) and you get set back to idle and win the game
             */
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
                            if (gameMode == GameMode.TWISTED) {
                                Collections.reverse(sequence);
                            }
                            if (seqLevel == 5) {
                                timeout = 1250;
                            } else if (seqLevel == 10) {
                                timeout = 1000;
                            } else if (seqLevel == 15) {
                                timeout = 750;
                            } else if (seqLevel == 20) {
                                timeout = 500;
                            } else if (seqLevel == 25) {
                                timeout = 250;
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
            case DESTROY:
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

    public void stateDestroy() {
        state = State.DESTROY;
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
        } else if (state == State.IDLE) {
            resetAllVariables();
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

        if (isrunning && timer != null) {
            timer.cancel();
            timer = null;
            isrunning = false;
        }
        if (isrunning && timeouttimer != null) {
            timeouttimer.cancel();
            timeouttimer = null;
            isrunning = false;
        }
        if (!isrunning && timeouttimer != null) {
            timeouttimer.cancel();
            timeouttimer = null;
        }
    }

    private void setAllDark() {
        for (int i = 1; i < 5; i++) {
            cd.setDarkColor(i);
        }
    }


    public void destroyGame() {
        cd.saveHighscore(getScore());
        resetAllVariables();
        stateDestroy();
        Log.d("Game destroyed", "clear");
    }

    private void resetAllVariables() {
        seqLevel = 1;
        seqCount = 1;
        sequence.clear();
        score = 0;
        stopTimeout();
        attracting = false;
        setAllDark();
        mPatternStepStartTimeMillis = 0;
    }

    @Override
    protected Void doInBackground(Void... Void) {

        Handler hand = new Handler();
        hand.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkState();

            }
        }, timeout);

        return null;
    }
}



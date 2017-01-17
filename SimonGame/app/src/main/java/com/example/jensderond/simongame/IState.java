package com.example.jensderond.simongame;

/**
 * Created by ruben on 16-1-2017.
 */

public interface IState {

    void stateIdle();
    int getScore();
    State getState();
    void stateStart();
    void stateShow();
    void statePlay();
    void stateLost();
    void stateWinner();
    void statePlaying();
    void sequenceHandler(int key);

}
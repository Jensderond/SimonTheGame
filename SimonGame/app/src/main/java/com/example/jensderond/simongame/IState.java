package com.example.jensderond.simongame;

/**
 * Created by ruben on 16-1-2017.
 */

public interface IState {

    public void stateIdle();
    public void stateStart();
    public void stateShow();
    public void statePlay();
    public void stateLost();
    public void stateWinner();
    public void sequenceHandler(int key);
}

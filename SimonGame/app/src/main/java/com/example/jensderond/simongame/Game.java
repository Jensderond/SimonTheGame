package com.example.jensderond.simongame;


/**
 * Created by jensderond on 17/01/2017.
 */


public interface Game {
    void init();
    void setLightColor(int color, boolean audio);
    void setDarkColor(int color);
    void disableButtons();
    void enableButtons();
    void setOnTouchListeners();

}

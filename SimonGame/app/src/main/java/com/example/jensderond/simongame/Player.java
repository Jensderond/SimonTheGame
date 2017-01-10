package com.example.jensderond.simongame;

import java.io.Serializable;

/**
 * Created by jensderond on 10/01/2017.
 */

public class Player implements Serializable {
    private String name;

    public Player(String name){
        this.setName(name);    
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

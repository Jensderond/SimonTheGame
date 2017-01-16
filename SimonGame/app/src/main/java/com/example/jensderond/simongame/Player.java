package com.example.jensderond.simongame;

import io.realm.RealmObject;

/**
 * Created by jensderond on 10/01/2017.
 */

public class Player extends RealmObject {
    private int id;
    private String name;
    private String gender;

    public Player(){}

    public Player(int id, String name, String gender){
        this.id = id;
        this.setName(name);
        this.setGender(gender);
    }

    public void setID(int id){
        this.id = id;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}

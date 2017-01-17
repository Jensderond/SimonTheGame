package com.example.jensderond.simongame;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by jensderond on 10/01/2017.
 */

public class Player extends RealmObject {
    @PrimaryKey @Required
    private String name;
    private String gender;

    public Player(){}

    public Player(String name, String gender){
        this.setName(name);
        this.setGender(gender);
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

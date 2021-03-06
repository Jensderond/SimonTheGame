package com.example.jensderond.simongame;

import java.util.Random;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import io.realm.annotations.Required;

/**
 * Created by jensderond on 10/01/2017.
 */


/**
 * Realmobject of players Database class
 */
public class Player extends RealmObject {
    @PrimaryKey
    @Required
    private String name;
    private String gender;

    /**
     * Empty Constructor, Necessary for RealmObject
     */
    public Player() {
    }

    /**
     * This is the constructor of the player
     *
     * @param name
     * @param gender
     */
    public Player(String name, String gender) {
        this.setName(name);
        this.setGender(gender);
    }

    /**
     * This returns the players name
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * This sets the players name
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * This returns the players gender
     *
     * @return gender
     */
    public String getGender() {
        return gender;
    }

    /**
     * This sets the players gender
     *
     * @param gender
     */
    public void setGender(String gender) {
        this.gender = gender;
    }

    /**
     * This function returns a int profile image
     *
     * @return
     */
    public int getImage() {
        int resource = R.mipmap.ic_male1;
        Random random = new Random();
        int randomNumber = random.nextInt(5 - 1) + 1;
        if (this.getGender().equals("female")) {
            switch (randomNumber) {
                case 1:
                    resource = R.mipmap.ic_female1;
                    break;
                case 2:
                    resource = R.mipmap.ic_female2;
                    break;
                case 3:
                    resource = R.mipmap.ic_female3;
                    break;
                case 4:
                    resource = R.mipmap.ic_female4;
                    break;
            }
        } else if (this.getGender().equals("male")) {
            switch (randomNumber) {
                case 1:
                    resource = R.mipmap.ic_male1;
                    break;
                case 2:
                    resource = R.mipmap.ic_male2;
                    break;
                case 3:
                    resource = R.mipmap.ic_male3;
                    break;
                case 4:
                    resource = R.mipmap.ic_male4;
                    break;
            }
        } else if (this.getGender().equals("other")) {
            resource = R.mipmap.ic_other1;
        }
        return resource;
    }
}

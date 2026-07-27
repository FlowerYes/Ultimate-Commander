package com.UC.game.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Unit extends Entity{
    public double health;
    public int size;
    public double cost;
    public double attackPower;
    public double defensePower;

    public double speed;

    public boolean longRange;
    public int index;
    public Sprite sprite;

    public void set_index(int index){
        this.index = index;
    }



}



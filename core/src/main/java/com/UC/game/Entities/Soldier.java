package com.UC.game.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;

public class Soldier extends Entity{
    public Sprite sprite;
    public boolean present;

    public Soldier(Sprite sprite){
         this.sprite = sprite;
         this.present = true;
    }
    public void disabled(){
        this.present= false;
    }

}

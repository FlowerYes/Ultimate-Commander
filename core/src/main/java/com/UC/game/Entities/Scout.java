package com.UC.game.Entities;

public class Scout extends Unit{
    public Scout(){
        this.health = 10 * size;
        this.size = 3;
        this.cost= 50;
        this.attackPower= 0;
        this.defensePower = 0.1 *size;
        this.speed = 0;

        this.longRange = true;

    }
}

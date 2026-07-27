package com.UC.game.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

public class Tank extends Unit {

    public Tank(Sprite sprite, float tileX, float tileY, float tileWidth, float tileHeight) {
        this.health = 1000;
        this.size = 1;
        this.cost = 250;
        this.attackPower = 100;
        this.defensePower = 0.5;
        this.speed = 1;
        this.longRange = false;
        this.present = true;

        // Clone the sprite so each tank has an independent one
        this.sprite = new Sprite(sprite);

        // Get width/height of tank
        float tankW = this.sprite.getWidth();
        float tankH = this.sprite.getHeight();

        // Randomly position the tank inside the tile
        float randomX = MathUtils.random(tileX, tileX + tileWidth - tankW);
        float randomY = MathUtils.random(tileY, tileY + tileHeight - tankH);

        this.sprite.setPosition(randomX, randomY);
    }

    public void drawTank(SpriteBatch batch) {
        if (this.present) {
            this.sprite.draw(batch);
        }
    }
    public void disable(){
        this.present=false;
    }
}

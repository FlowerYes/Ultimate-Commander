package com.UC.game.Entities;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;

/**
 * "Soldiers" now represents a platoon of multiple sub-soldiers.
 */
public class Soldiers extends Unit {
    public Soldier[] platoon;  // array of sub-soldiers

    // Let's define how many sub-soldiers in a platoon:
    public int platoonSize = 10;

    /**
     * Constructor
     * @param baseSprite The base sprite to copy for each sub-soldier
     * @param tileX The left/bottom corner of the tile (or any region)
     * @param tileY
     * @param tileWidth The width of the region in which soldiers can appear
     * @param tileHeight
     */
    public Soldiers(Sprite baseSprite,
                    float tileX, float tileY,
                    float tileWidth, float tileHeight) {

        // You can keep or adjust these stats as needed
        this.size         = 10;  // if you want 'size' to match your platoon size
        this.health       = 10 * size;
        this.cost         = 10;
        this.attackPower  = 10 * size;
        this.defensePower = 0.1f * size;
        this.speed        = 0.5f;
        this.longRange    = false;

        // Instead of storing just "this.sprite", let's store an array of sub-soldiers:
        platoon = new Soldier[platoonSize];

        // Create each sub-soldier with its own copy of the sprite
        for (int i = 0; i < platoonSize; i++) {
            // Copy the base sprite so each soldier has its own transform
            Sprite soldierSprite = new Sprite(baseSprite);

            // Place the soldier at a random offset within [tileX, tileX+tileWidth], [tileY, tileY+tileHeight]
            // Sub-soldier is 1x1 if baseSprite is 1x1
            float soldierW = soldierSprite.getWidth();
            float soldierH = soldierSprite.getHeight();

            float randomX = MathUtils.random(tileX,  tileX + tileWidth  - soldierW);
            float randomY = MathUtils.random(tileY,  tileY + tileHeight - soldierH);

            soldierSprite.setPosition(randomX, randomY);

            // Now create a new Soldier entity that holds that sprite
            Soldier sub = new Soldier(soldierSprite);
            platoon[i] = sub;
        }
    }

    /**
     * Draws all sub-soldiers in this platoon.
     */
    public void drawPlatoon(SpriteBatch batch) {
        // Loop over the array of Soldier objects
        for (Soldier s : platoon) {
            // If not disabled, draw
            if (s.present) {
                s.sprite.draw(batch);
            }
        }
    }
    public void remove_size(int amt){
        if(amt<platoonSize){
            this.size -= amt;
        }
        for(int i = 0; i< amt; i++){
            int index_removed =  MathUtils.random(0,amt);
            this.platoon[index_removed].present = false;
        }
    }
    public void disable(){
        for(int i = 0; i< platoonSize; i++){
            this.platoon[i].present = false;
        }
    }
}

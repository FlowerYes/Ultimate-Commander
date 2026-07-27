package com.UC.game.MapComponents;

import com.UC.game.Entities.Unit;
import java.util.Arrays;

public class Tile {
    public int tile_size;
    public int x_coordinate;
    public int lane_number;
    public Unit[] units;
    public int num_troops;
    public boolean full;
    // You could also have y_coordinate or other properties here.

    public Tile(int tile_size, int x_coordinate, int lane) {
        this.num_troops = 0;
        this.units = new Unit[3];
        this.tile_size = tile_size;
        this.x_coordinate = x_coordinate;
        this.lane_number = lane;
        this.full = false;
    }
    public boolean addUnit(Unit unit){

        if(this.num_troops < 3){
            unit.set_index(num_troops);
            this.units[num_troops] = unit;
            num_troops+=1;
            if(this.num_troops ==3){
                this.full = true;
            }
            return true;
        }else{
            return false;
        }
    }
    public void remove(int index) {

        if (index < 0 || index >= num_troops) {
            System.out.println("Invalid index. Cannot remove unit.");
            return;
        }


        for (int i = index; i < num_troops ; i++) {
            units[i] = units[i + 1];
            units[i].set_index(i);
        }

        units[num_troops - 1] = null;


        num_troops--;
        this.full = false;

    }
}



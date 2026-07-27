package com.UC.game.MapComponents;

public class Lane {
    public double top;
    public double bottom;
    public double left;
    public double right;
    public double length;
    public double width;
    public int amt_tiles;
    public double tile_size;
    public boolean selected;
    public int selected_tile;
    public int lane_number;

    // Array of Tiles
    public Tile[] tiles;

    /**
     * A constructor that sets all required fields.
     */
    public Lane(double top, double bottom, double left, double right, int amt_tiles, int lane_number) {
        this.top = top;
        this.bottom = bottom;
        this.left = left;
        this.right = right;
        this.amt_tiles = amt_tiles;
        this.lane_number= lane_number;

        // Calculate length and width
        this.length = right - left;      // horizontally
        this.width = bottom - top;       // vertically (may be negative if top > bottom depending on your coordinate system)

        // Calculate tile size
        // For instance, if we place tiles along the length (horizontal axis):
        this.tile_size = this.length / amt_tiles;

        // Initialize the Tile array
        this.tiles = new Tile[amt_tiles];

        // Fill the array. For example, we can set each Tile’s size and x_coordinate.
        for (int i = 0; i < amt_tiles; i++) {
            int currentTileX = (int) (left + i * tile_size);
            int currentTileSize = (int) tile_size;  // You might cast to int or store as double

            // Create a new Tile and store it in the array
            this.tiles[i] = new Tile(currentTileSize, currentTileX,lane_number);
        }
    }
    public void select(int tile_index){
        if(this.selected){
            selected_tile = tile_index;
        }
    }

}

package com.UC.game;

import com.UC.game.Entities.Tank;
import com.UC.game.MapComponents.Lane;
import com.UC.game.MapComponents.Map;
import com.UC.game.MapComponents.Tile;
import com.UC.game.Entities.Soldiers;
import com.UC.game.Entities.Unit;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.UC.game.MapComponents.overlayUI;

public class Main implements ApplicationListener {

    public static final int WORLD_WIDTH  = 100;
    public static final int WORLD_HEIGHT = 56;

    private overlayUI overlayUI;
    private Viewport viewport;
    private ShapeRenderer sr;
    private boolean drawGrid = true;

    private OrthographicCamera cam;
    private SpriteBatch batch;

    private Sprite mapSprite;
    private float rotationSpeed;
    private Map map;

    private Texture soldierTexture;
    private Sprite soldierBaseSprite;

    private Texture tankTexture;
    private Sprite tankBaseSprite;

    @Override
    public void create() {
        rotationSpeed = 0.5f;
        overlayUI = new overlayUI();

        // Load the background map
        mapSprite = new Sprite(new Texture(Gdx.files.internal("photo_2025-02-05_01-55-48.jpg")));
        mapSprite.setPosition(0, 0);
        mapSprite.setSize(WORLD_WIDTH, WORLD_HEIGHT);

        sr = new ShapeRenderer();
        cam = new OrthographicCamera();
        viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT, cam);
        cam.position.set(cam.viewportWidth / 2f, cam.viewportHeight / 2f, 0);
        cam.update();

        batch = new SpriteBatch();

        // Load soldier
        soldierTexture = new Texture(Gdx.files.internal("German_soldier_MP40_2.png"));
        soldierBaseSprite = new Sprite(soldierTexture);
        soldierBaseSprite.setSize(1, 1); // 1x1 world units

        // Load tank
        tankTexture = new Texture(Gdx.files.internal("german_tank_2.png"));
        tankBaseSprite = new Sprite(tankTexture);
        tankBaseSprite.setSize(4, 4); // 4x4 world units

        // Create Lanes
        Lane lane1 = new Lane(16,  8, 0, 100, 10, 0);
        Lane lane2 = new Lane(32, 24, 0, 100, 10, 1);
        Lane lane3 = new Lane(48, 40, 0, 100, 10, 2);
        Lane[] lanes = {lane1, lane2, lane3};

        map = new Map(3, lanes);
    }

    @Override
    public void render() {
        handleInput();
        cam.update();

        // Clear screen
        Gdx.gl.glClearColor(0, 0, 0, 0);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // 1) Draw background + soldiers
        batch.setProjectionMatrix(cam.combined);
        batch.begin();

        mapSprite.draw(batch);
        drawSoldiers(); // <-- now we only draw them, no position changes here

        batch.end();

        // 2) Optionally draw grid lines
        if (drawGrid) {
            sr.setProjectionMatrix(cam.combined);
            drawGrid();
        }

        // 3) Handle highlight + spawn soldier on click
        drawHoveredTile();

        // 4) Render overlay/UI last
        overlayUI.render();
    }

    /**
     * Draw the soldiers by iterating lanes/tiles/units.
     * We NO LONGER set random positions here; that is done at spawn time.
     */
    private void drawSoldiers() {
        for (Lane lane : map.Lanes) {
            for (int i = 0; i < lane.amt_tiles; i++) {
                Tile tile = lane.tiles[i];
                if (tile == null) continue;

                for (int u = 0; u < tile.num_troops; u++) {
                    Unit unit = tile.units[u];
                    if (unit == null) continue;

                    // If it's a Soldiers object (a platoon), cast and draw
                    if (unit instanceof Soldiers) {
                        ((Soldiers) unit).drawPlatoon(batch);
                    }
                    if(unit instanceof Tank){
                        ((Tank) unit).drawTank(batch);
                    }
                    // else if it's some other unit type, draw differently, etc.
                }
            }
        }
    }


    /**
     * Check which tile is hovered. If left-clicked, spawn a Soldier with a RANDOM position
     * inside that tile.
     */
    private void drawHoveredTile() {
        // Convert mouse to world coords
        Vector3 mousePos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
        cam.unproject(mousePos);

        sr.setProjectionMatrix(cam.combined);
        for (Lane lane : map.Lanes) {
            double laneLeft   = lane.left;
            double laneRight  = lane.right;
            double laneBottom = lane.bottom;
            double laneTop    = lane.top;

            boolean withinX = (mousePos.x >= laneLeft && mousePos.x <= laneRight);
            boolean withinY = (mousePos.y >= laneBottom && mousePos.y <= laneTop);

            if (withinX && withinY) {
                // Which tile index is hovered?
                int hoveredTileIndex = (int)((mousePos.x - laneLeft) / lane.tile_size);
                if (hoveredTileIndex >= 0 && hoveredTileIndex < lane.amt_tiles) {

                    // Highlight the hovered tile
                    sr.begin(ShapeRenderer.ShapeType.Filled);
                    if(lane.tiles[hoveredTileIndex].full){
                        sr.setColor(Color.RED);

                    }else{
                        sr.setColor(Color.GREEN);
                    }


                    float tileX      = (float)(laneLeft + hoveredTileIndex * lane.tile_size);
                    float tileY      = (float) laneBottom;
                    float tileWidth  = (float) lane.tile_size;
                    float tileHeight = (float) (laneTop - laneBottom);

                    sr.rect(tileX, tileY, tileWidth, tileHeight);
                    sr.end();

                    // If user clicks, spawn 10 Soldiers at random offsets
                    if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
                        // In your click event:
                        Tile clickedTile = lane.tiles[hoveredTileIndex];
                        if(clickedTile.full){
                            for(Unit u: clickedTile.units){
                                if (u instanceof Soldiers) {
                                    ((Soldiers) u).disable();
                                }
                                if(u instanceof Tank){
                                    ((Tank) u).disable();
                                }
                            }
                        }
                        if(overlayUI.buttonSlected[0]){
                            if (clickedTile != null) {
                                // Suppose tileX, tileY, tileWidth, tileHeight describe the tile region
                                Soldiers newPlatoon = new Soldiers(
                                    soldierBaseSprite,
                                    tileX, tileY,
                                    tileWidth, tileHeight
                                );

                                // Then add to the tile
                                boolean added = clickedTile.addUnit(newPlatoon);
                                if (added) {
                                    System.out.println("Spawned a platoon of 10 soldiers in tile!");
                                } else {
                                    System.out.println("Tile is full, can't add more!");
                                }
                            }
                        }
                        if(overlayUI.buttonSlected[1]){
                            if (clickedTile != null) {
                                // Suppose tileX, tileY, tileWidth, tileHeight describe the tile region
                                Tank newTank = new Tank(tankBaseSprite, tileX, tileY,
                                    tileWidth, tileHeight);

                                // Then add to the tile
                                boolean added = clickedTile.addUnit(newTank);
                                if (added) {
                                    System.out.println("Spawned a tank in tile!");
                                } else {
                                    System.out.println("Tile is full, can't add more!");
                                }
                            }
                        }

                    }
                }
            }
        }
    }


    private void drawGrid() {
        sr.begin(ShapeRenderer.ShapeType.Line);
        sr.setColor(Color.WHITE);

        for (int x = 0; x <= WORLD_WIDTH; x++) {
            sr.line(x, 0, x, WORLD_HEIGHT);
        }
        for (int y = 0; y <= WORLD_HEIGHT; y++) {
            sr.line(0, y, WORLD_WIDTH, y);
        }

        sr.end();
    }

    private void handleInput() {
        float moveSpeed = 3 * cam.zoom;
        if (Gdx.input.isKeyPressed(Input.Keys.E)) {
            cam.zoom += 0.02F;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.Q)) {
            cam.zoom -= 0.02F;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            cam.translate(-moveSpeed, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            cam.translate(moveSpeed, 0, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            cam.translate(0, -moveSpeed, 0);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            cam.translate(0, moveSpeed, 0);
        }

        // Clamp zoom and camera bounds
        cam.zoom = MathUtils.clamp(cam.zoom, 0.1f, 100 / cam.viewportWidth);

        float effectiveViewportWidth  = cam.viewportWidth  * cam.zoom;
        float effectiveViewportHeight = cam.viewportHeight * cam.zoom;

        cam.position.x = MathUtils.clamp(
            cam.position.x,
            effectiveViewportWidth  / 2f,
            WORLD_WIDTH  - effectiveViewportWidth  / 2f
        );
        cam.position.y = MathUtils.clamp(
            cam.position.y,
            effectiveViewportHeight / 2f,
            WORLD_HEIGHT - effectiveViewportHeight / 2f
        );
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    @Override
    public void dispose() {
        mapSprite.getTexture().dispose();
        soldierTexture.dispose();
        tankTexture.dispose();
        batch.dispose();
        sr.dispose();
        overlayUI.dispose();
    }

    @Override
    public void pause() { }

    @Override
    public void resume() { }
}

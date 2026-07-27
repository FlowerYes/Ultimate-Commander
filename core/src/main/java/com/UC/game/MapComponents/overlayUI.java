package com.UC.game.MapComponents ;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.viewport.FitViewport;

import static com.UC.game.Main.WORLD_HEIGHT;
import static  com.UC.game.Main.WORLD_WIDTH;

public class overlayUI {
    private Stage stage;
    private BitmapFont font;
    private TextButton.TextButtonStyle soldierButtonStyle;
    private TextButton.TextButtonStyle tankButtonStyle;
    public boolean[] buttonSlected= {false,false};

    public overlayUI() {
        // Create a single viewport for the stage
        FitViewport viewport = new FitViewport(WORLD_WIDTH, WORLD_HEIGHT);
        stage = new Stage(viewport);

        // Initialize font with appropriate scaling
        font = new BitmapFont();
        font.getData().setScale(0.1f);

        // Load the texture (UI background)
        Texture uiTexture = new Texture(Gdx.files.internal("Battle_UI_3.png"));
        TextureRegion uiRegion = new TextureRegion(uiTexture);
        TextureRegionDrawable uiDrawable = new TextureRegionDrawable(uiRegion);
        Image uiImage = new Image(uiDrawable);

        // Make the UI background block input
        uiImage.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                return true;
            }
        });

        // Soldier button setup
        Texture soldierTexture = new Texture(Gdx.files.internal("German_soldier_MP40_2.png"));
        TextureRegion soldierRegion = new TextureRegion(soldierTexture);
        TextureRegionDrawable soldierDrawable = new TextureRegionDrawable(soldierRegion);

        // Create soldier button style
        soldierButtonStyle = new TextButton.TextButtonStyle();
        soldierButtonStyle.up = soldierDrawable;
        soldierButtonStyle.down = soldierDrawable;
        soldierButtonStyle.font = font;

        // Tank button setup
        Texture tankTexture = new Texture(Gdx.files.internal("german_tank_2.png"));
        TextureRegion tankRegion = new TextureRegion(tankTexture);
        TextureRegionDrawable tankDrawable = new TextureRegionDrawable(tankRegion);

        // Create tank button style
        tankButtonStyle = new TextButton.TextButtonStyle();
        tankButtonStyle.up = tankDrawable;
        tankButtonStyle.down = tankDrawable;
        tankButtonStyle.font = font;

        // Create soldier button
        TextButton Unit1Button = new TextButton("", soldierButtonStyle);
        Unit1Button.setSize(8, 8);
        Unit1Button.setPosition(19, 49);

        // Create tank button
        TextButton Unit2Button = new TextButton("", tankButtonStyle);
        Unit2Button.setSize(8, 8);
        Unit2Button.setPosition(28, 49);  // Positioned slightly to the right of Unit1Button

        // Add click listener to soldier button
        Unit1Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Soldier button clicked!");
                buttonSlected[0]=!buttonSlected[0];
                buttonSlected[1]=false;

                return true;
            }
        });

        // Add click listener to tank button
        Unit2Button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                System.out.println("Tank button clicked!");
                buttonSlected[1]=!buttonSlected[1];
                buttonSlected[0]=false;
                return true;
            }
        });

        // Set UI image size and position
        uiImage.setSize(WORLD_WIDTH, WORLD_HEIGHT);
        uiImage.setPosition(0, 0);

        // Add actors to stage in correct order (bottom to top)
        stage.addActor(uiImage);
        stage.addActor(Unit1Button);
        stage.addActor(Unit2Button);  // Don't forget to add the tank button to the stage

        // Set this stage as the input processor
        Gdx.input.setInputProcessor(stage);
    }

    public void render() {
        stage.getViewport().update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);
        stage.act(Gdx.graphics.getDeltaTime());
        stage.draw();
    }

    public void dispose() {
        stage.dispose();
        if (font != null) {
            font.dispose();
        }
    }

    public Stage getStage() {
        return stage;
    }
}

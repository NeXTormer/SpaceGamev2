package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import me.spacegame.SpaceGame;

/**
 * Created by Michi on 15.05.2017.
 */

public class Player {

    private int baseSpeed = 12;
    private float x;
    private float y;
    private int height;
    private int width;
    private Circle box;
    private int health = 100;
    private Texture texture;

    public Player()
    {
        width = 150;
        height = 150;
        x = SpaceGame.VIEWPORTWIDTH/3;
        y = SpaceGame.VIEWPORTHEIGHT/2-(height/2);
        box = new Circle(x, y, width);
        texture = new Texture(Gdx.files.internal("gameobjects/SpaceShip_01.png"));
    }

    public void render(float delta, SpriteBatch batch)
    {
        batch.draw(texture, x, y, height, width);
    }

    public void updatePosition(Touchpad touchpad)
    {
        x += touchpad.getKnobPercentX() * baseSpeed;
        y += touchpad.getKnobPercentY() * baseSpeed;
    }

    public void dispose()
    {
    }
}

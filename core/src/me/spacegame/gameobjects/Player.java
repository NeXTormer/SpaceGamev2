package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

import me.spacegame.SpaceGame;

/**
 * Created by Michi on 15.05.2017.
 */

public class Player {

    private int speed;
    private float x;
    private float y;
    private int height;
    private int width;
    private Circle box;
    private int health;
    private Texture texture;

    public Player()
    {
        width = 50;
        height = 50;
        x = SpaceGame.VIEWPORTWIDTH/3;
        y = SpaceGame.VIEWPORTHEIGHT/2-(height/2);
        box = new Circle(x, y, width);
        texture = new Texture(Gdx.files.internal("gameobjects/SpaceShip_01.png"));
    }

    public void render(float delta, SpriteBatch batch)
    {

    }

    public void dispose()
    {
    }
}

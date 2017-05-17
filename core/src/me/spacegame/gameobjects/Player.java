package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import me.spacegame.SpaceGame;

/**
 * Created by Michi on 15.05.2017.
 */

public class Player {

    public float x;
    public float y;
    public int height;
    public int width;
    public Rectangle box;
    public int health = 100;

    private int baseSpeed = 12;
    private float newx;
    private float newy;
    private Texture texture;

    public Player()
    {
        width = 150;
        height = 150;
        x = SpaceGame.VIEWPORTWIDTH/3;
        y = SpaceGame.VIEWPORTHEIGHT/2-(height/2);
        box = new Rectangle(x, y, width, height);
        texture = new Texture(Gdx.files.internal("gameobjects/SpaceShip_01.png"));
    }

    public void render(float delta, SpriteBatch batch)
    {
        batch.draw(texture, x, y, height, width);
    }

    public void updatePosition(Touchpad touchpad)
    {
        newx=touchpad.getKnobPercentX() * baseSpeed+x;
        newy=touchpad.getKnobPercentY() * baseSpeed+y;
        if(newx+width<SpaceGame.VIEWPORTWIDTH && newx>0)
        {
            x = newx;
        }
        if(newy+height<SpaceGame.VIEWPORTHEIGHT && newy>0)
        {
            y = newy;
        }
        box.setPosition(x, y);
    }

    public void dispose()
    {
        texture.dispose();
    }
}

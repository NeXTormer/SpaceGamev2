package me.spacegame.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import me.spacegame.gameobjects.Meteor;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 21.06.2017.
 */

public class PowerUpObject {

    private Meteor m;
    public float width;
    private float height;
    public float x;
    private float y;
    private int speed;
    public Rectangle box;
    private Texture texture;
    private GameScreen gamescreen;

    public PowerUpObject(Meteor m, GameScreen gs)
    {
        gamescreen = gs;
        this.m = m;
        y = m.y;
        x = m.x;
        speed=m.speed;
        //texture = gs.hb.fbo.getColorBufferTexture();
        texture = gamescreen.getGame().getTexture("questionmark");
        width=texture.getWidth();
        height=texture.getHeight();
        box = new Rectangle(x, y, width, height);
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(texture, x, y, width, height);
    }

    public void update()
    {
        x-=speed;
        box.setX(x);
    }

    public static void dispose()
    {

    }
}

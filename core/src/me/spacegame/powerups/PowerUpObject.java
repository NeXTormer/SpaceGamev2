package me.spacegame.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

import me.spacegame.gameobjects.Meteor;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 21.06.2017.
 */

public class PowerUpObject {

    private Meteor m;
    private float width;
    private float height;
    private float x;
    private float y;
    private int speed;
    public Circle box;
    private Texture texture;


    public PowerUpObject(Meteor m, GameScreen gs)
    {
        this.m = m;
        y = m.y;
        x = m.x;
        speed=m.speed;
        texture = gs.hb.fbo.getColorBufferTexture();
        width=texture.getWidth();
        height=texture.getHeight();
        box = new Circle(x+(width/2), y+(height/2), width/2);
    }

    public void render(float delta, SpriteBatch batch)
    {
        x-=speed;
        batch.draw(texture, x, y);
    }

    public static void dispose()
    {

    }
}

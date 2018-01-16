package me.spacegame.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import me.spacegame.animations.QuestionMark;
import me.spacegame.gameobjects.Meteor;
import me.spacegame.screens.GameScreen;
import me.spacegame.util.Scale;

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

    private QuestionMark qm;

    public PowerUpObject(Meteor m, GameScreen gs)
    {
        gamescreen = gs;
        this.m = m;
        y = m.y;
        x = m.x;
        speed=m.speed;
        //texture = gs.hb.fbo.getColorBufferTexture();
        qm = new QuestionMark(x, y, gs);
       // texture = gamescreen.getGame().getTexture("questionmark");
        width= Scale.getScaledSizeX(100);
        height=Scale.getScaledSizeY(100);
        box = new Rectangle(x, y, width, height);
    }

    public void draw(SpriteBatch batch)
    {
        qm.draw(batch);
    }

    public void update()
    {
        qm.update();
        x-=speed;
        box.setX(x);
        qm.setPosition(x, y);
    }

    public static void dispose()
    {

    }
}

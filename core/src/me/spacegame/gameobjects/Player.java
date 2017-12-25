package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.Touchpad;

import java.util.ArrayList;
import java.util.List;

import me.spacegame.SpaceGame;
import me.spacegame.screens.GameScreen;
import me.spacegame.util.Scale;

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
    public boolean dead = false;
    public int score;

    public int baseSpeed = 12;
    private float newx;
    private float newy;
    private Texture texture;

    public boolean visible = true;
    private GameScreen gameScreen;

    public Player(GameScreen screen)
    {
        width = (int) Scale.getScaledSizeX(150);
        height = (int) Scale.getScaledSizeY(120);
        score=0;
        x = SpaceGame.VIEWPORTWIDTH/3;
        y = SpaceGame.VIEWPORTHEIGHT/2-(height/2);
        box = new Rectangle(x, y, width, height);
        texture = screen.getGame().getTexture("spaceship1");
        gameScreen = screen;
    }

    public void draw(SpriteBatch batch)
    {
        if(visible)
            batch.draw(texture, x, y, width, height);
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

    public GameScreen getGameScreen() { return gameScreen; }

    public void dispose()
    {
        texture.dispose();
    }

    public void setVisible(boolean vis) { visible = vis; }
}

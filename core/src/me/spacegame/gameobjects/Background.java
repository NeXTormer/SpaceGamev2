package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.screens.GameScreen;
import me.spacegame.util.Scale;

/**
 * Created by Felix on 09-May-17.
 */

public class Background {

    private Texture bg;

    private int speed = (int) Scale.getScaledSizeX(3);
    private int x = 0;

    private GameScreen gameScreen;

    private float height = Scale.getScaledSizeX(1080);
    private float width = Scale.getScaledSizeY(1920);


    public Background(GameScreen gameScreen)
        {
        this.gameScreen = gameScreen;
        bg = gameScreen.game.getTexture("background");
    }


    public void draw(SpriteBatch batch)
    {
        batch.draw(bg, x, 0, width, height);
        batch.draw(bg, x + (width), 0, width, height);
    }

    public void update()
    {
        x -= speed;
        if(x <= -bg.getWidth()) x = 0;
    }

    public void dispose()
    {
        bg.dispose();
    }


}

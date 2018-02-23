package me.spacegame.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 23.02.2018.
 */

public class Laser
{
    private static Texture spritesheet;
    private static TextureRegion[] textureRegions;

    private float elapsed = 0f;
    private float x = 0;
    private float y = 0;
    private int width = 80;
    private int height = 130;
    private float frameDuration = 1.4f;
    public int elapsedFrames = 0;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animation;

    private boolean firstInit = false;


    public Laser(float x, float y, GameScreen screen)
    {
        if(!firstInit)
        {
            spritesheet = screen.getGame().getTexture("laser1");
            textureRegions = new TextureRegion[4 * 6];


            TextureRegion[][] temp = TextureRegion.split(spritesheet, width, height);
            for(int i = 0; i < 4; i++)
            {
                for(int j = 0; j < 6; j++)
                {
                    textureRegions[i * 6 + j] = temp[i][j];
                }
            }
            firstInit = true;
        }

        this.x = x;
        this.y = y;


        animation = new Animation<TextureRegion>(10, textureRegions);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animation.setFrameDuration(frameDuration);
    }

    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    public void setFrameDuration(float duration)
    {
        frameDuration = duration;
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(animation.getKeyFrame(elapsedFrames), x, y, width, height);
    }

    public boolean update()
    {
        elapsedFrames++;
        elapsed += Gdx.graphics.getDeltaTime();

        return true;
    }


    public static void dispose()
    {
        spritesheet.dispose();
    }
}


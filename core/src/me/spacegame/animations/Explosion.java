package me.spacegame.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation;

import me.spacegame.SpaceGame;
import me.spacegame.screens.GameScreen;

/**
 * Created by Felix on 18-May-17.
 */

public class Explosion {

    private static Texture spritesheet;
    private static TextureRegion[] textureRegions;

    static
    {
        spritesheet = SpaceGame.getInstance().getTexture("explosion1");
        textureRegions = new TextureRegion[3 * 3];

        TextureRegion[][] temp = TextureRegion.split(spritesheet, 80, 80);
        for(int i = 0; i < 3; i++)
        {
            for(int j = 0; j < 3; j++)
            {
                textureRegions[i * 3 + j] = temp[i][j];
            }
        }
    }

    private float elapsed = 0f;
    private int x = 0;
    private int y = 0;
    private int width = 230;
    private int height = 230;
    private float frameDuration = 1.9f;
    private int elapsedFrames = 0;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animation;
    private boolean big = false;

    public Explosion(int x, int y, int width, int height, GameScreen screen)
    {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;


        animation = new Animation<TextureRegion>(10, textureRegions);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        animation.setFrameDuration(frameDuration);
        this.big = false;
    }

    public Explosion(int x, int y, boolean big)
    {
        this.x = x;
        this.y = y;


        animation = new Animation<TextureRegion>(10, textureRegions);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
        animation.setFrameDuration(frameDuration);
        this.big = big;
    }

    public void setPosition(int x, int y)
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
        if(big)
        {
            batch.draw(animation.getKeyFrame(elapsedFrames), x, y, width * 3, height * 3);
        }
        else
        {
            batch.draw(animation.getKeyFrame(elapsedFrames), x, y, width, height);
        }
    }

    public boolean update()
    {
        elapsedFrames++;
        elapsed += Gdx.graphics.getDeltaTime();

        return animation.isAnimationFinished(elapsedFrames);
    }

    public static void dispose()
    {
        spritesheet.dispose();
    }
}

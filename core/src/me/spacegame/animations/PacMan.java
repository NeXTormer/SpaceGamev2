package me.spacegame.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation;

import me.spacegame.screens.GameScreen;

/**
 * Created by Felix on 18-May-17.
 */

public class PacMan {

    private static int width = 40;
    private static int height = 40;

    private int drawWidth;
    private int drawHeight;
    private static TextureRegion[] textureRegions;

    private Texture spritesheet;
    private boolean firstInit = false;


    private float elapsed = 0f;
    private int x = 0;
    private int y = 0;
    private float frameDuration = 3.4f;
    private int elapsedFrames = 0;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animation;

    public PacMan(int x, int y, GameScreen screen)
    {
        if(!firstInit)
        {
            spritesheet = screen.getGame().getTexture("pacman");
            textureRegions = new TextureRegion[3 * 3];


            TextureRegion[][] temp = TextureRegion.split(spritesheet, width, height);
            textureRegions = temp[0];
            firstInit = true;
        }


        this.x = x;
        this.y = y;
        this.drawWidth=250;
        this.drawHeight=250;

        animation = new com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>(10, textureRegions);
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        animation.setFrameDuration(frameDuration);
    }

    public void setPosition(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.drawWidth = width;
        this.drawHeight = height;
    }

    public void setFrameDuration(float duration)
    {
        frameDuration = duration;
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(animation.getKeyFrame(elapsedFrames), x, y, drawWidth, drawHeight);
    }

    public void update()
    {
        elapsedFrames++;
        elapsed += Gdx.graphics.getDeltaTime();
    }
}

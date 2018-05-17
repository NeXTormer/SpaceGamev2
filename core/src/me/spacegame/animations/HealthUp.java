package me.spacegame.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 17.05.2018.
 */

public class HealthUp {

    private static Texture spritesheet;
    private static TextureRegion[] textureRegions;

    private float elapsed = 0f;
    private float x = 0;
    private float y = 0;
    private float width = 300;
    private float height = 300;
    private float frameDuration = 1.9f;
    private int elapsedFrames = 0;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animation;
    private boolean firstInit = false;


    public HealthUp(int x, int y, int width, int height, GameScreen screen)
    {
        if(!firstInit)
        {
            spritesheet = screen.getGame().getTexture("healthupanimation");
            textureRegions = new TextureRegion[3 * 3];

            TextureRegion[][] temp = TextureRegion.split(spritesheet, 80, 80);
            for(int i = 0; i < 3; i++)
            {
                for(int j = 0; j < 3; j++)
                {
                    textureRegions[i * 3 + j] = temp[i][j];
                }
            }
            firstInit = true;
        }

        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;


        animation = new Animation<TextureRegion>(10, textureRegions);
        animation.setPlayMode(Animation.PlayMode.LOOP_PINGPONG);
        animation.setFrameDuration(frameDuration);
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
        batch.draw(animation.getKeyFrame(elapsedFrames), x, y, width, height);
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


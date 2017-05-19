package me.spacegame.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by Felix on 18-May-17.
 */

public class Explosion {

    private static Texture spritesheet;
    private static TextureRegion[] textureRegions;

    private float elapsed = 0f;
    private int x = 0;
    private int y = 0;
    private int width = 80;
    private int height = 80;
    private float frameDuration = 1.9f;
    private int elapsedFrames = 0;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animation;

    static
    {
        spritesheet = new Texture(Gdx.files.internal("animations/explosion1.png"));
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


    public Explosion(int x, int y)
    {
        this.x = x;
        this.y = y;


        animation = new Animation<TextureRegion>(10, textureRegions);
        animation.setPlayMode(Animation.PlayMode.NORMAL);
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

    public boolean draw(float delta, SpriteBatch batch)
    {
        elapsedFrames++;
        elapsed += Gdx.graphics.getDeltaTime();

        batch.draw(animation.getKeyFrame(elapsedFrames), x, y, 230, 230);

        return animation.isAnimationFinished(elapsedFrames);
    }

    public static void dispose()
    {
        spritesheet.dispose();
    }
}

package me.spacegame.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by Felix on 18-May-17.
 */

public class Explosion {

    private Texture spritesheet;
    private TextureRegion[] textureRegions;

    private float elapsed = 0f;
    private int x = 0;
    private int y = 0;
    private int width = 80;
    private int height = 80;
    private int elapsedFrames;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animation;


    public Explosion(int x, int y)
    {
        this.x = x;
        this.y = y;
        textureRegions = new TextureRegion[3 * 3];
        animation = new Animation<TextureRegion>(10, textureRegions);

    }

    public void setPosition(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public boolean draw(float delta, SpriteBatch batch)
    {
        elapsedFrames++;
        elapsed += Gdx.graphics.getDeltaTime();
        batch.draw(animation.getKeyFrame(elapsed, false), x, y, width, height);
        return elapsedFrames > (3 * 3);
    }


}

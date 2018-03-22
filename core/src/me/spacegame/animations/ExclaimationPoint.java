package me.spacegame.animations;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.g2d.Animation;

import me.spacegame.screens.GameScreen;
import me.spacegame.util.Scale;

/**
 * Created by Felix on 18-May-17.
 */

public class ExclaimationPoint {

    private static int width = 50;
    private static int height = 150;
    private static TextureRegion[] textureRegions;

    private Texture spritesheet;
    private boolean firstInit = false;


    private float elapsed = 0f;
    private int x = 0;
    private int y = 0;
    private float frameDuration = 1.9f;
    private int elapsedFrames = 0;
    private com.badlogic.gdx.graphics.g2d.Animation<TextureRegion> animation;

    public ExclaimationPoint(int x, int y, GameScreen screen)
    {
        if(!firstInit)
        {
            spritesheet = screen.getGame().getTexture("exclaimationpoint");
            textureRegions = new TextureRegion[5 * 1];


            TextureRegion[][] temp = TextureRegion.split(spritesheet, width, height);
            textureRegions = temp[0];
            firstInit = true;
        }


        this.x = x;
        this.y = y;

        animation = new com.badlogic.gdx.graphics.g2d.Animation<TextureRegion>(10, textureRegions);
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

    public void update()
    {
        elapsedFrames++;
        elapsed += Gdx.graphics.getDeltaTime();
    }

}

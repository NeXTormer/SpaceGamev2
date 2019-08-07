package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import me.spacegame.SpaceGame;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 12.04.2018.
 */

public class ShipPart
{
    private static int width = 30;
    private static int height = 30;

    private static TextureRegion[] textureRegions;
    private static Texture spritesheet;

    static
    {
        spritesheet = SpaceGame.getInstance().getTexture("shippart");
        textureRegions = new TextureRegion[10];

        TextureRegion[][] temp = TextureRegion.split(spritesheet, width, height);
        textureRegions = temp[0];
    }

    private int drawWidth;
    private int drawHeight;
    private float x = 0;
    private float y = 0;
    private float deg = 0;
    public int speed = 8;

    public ShipPart(float x, float y, float deg, GameScreen screen)
    {
        this.x = x;
        this.y = y;
        this.drawWidth=80;
        this.drawHeight=80;
        this.deg = deg;

    }

    public void setPosition(int x, int y, int width, int height)
    {
        this.x = x;
        this.y = y;
        this.drawWidth = width;
        this.drawHeight = height;
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(textureRegions[GameScreen.random.nextInt(9)], x, y, drawWidth, drawHeight);
    }

    public void update()
    {
        float sX =  speed*((float)Math.cos(deg));
        float sY =  speed*((float)Math.sin(deg));

        x+=sX;
        y+=sY;
    }
}
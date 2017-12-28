package me.spacegame.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

import java.util.Random;

import me.spacegame.SpaceGame;
import me.spacegame.screens.GameScreen;
import me.spacegame.util.Scale;

/**
 * Created by Michi on 10.05.2017.
 */

public class Meteor {


    private static Texture[] meteorFull;
    private static Texture[] meteorDamaged;
    private boolean firstInit = false;

    private static final int METEOR_TEXTURES = 6;

    public float x;
    public float y;
    public float radius;
    public int damage;
    public int health;

    public long lastTimeHit = 0;
    public Circle box;
    private Sprite meteorsprite;
    public int speed;
    private int saveSpeed;
    private int rotateSpeed;
    private int texture;

    public Meteor(GameScreen screen)
    {
        if(!firstInit)
        {
            meteorFull = new Texture[METEOR_TEXTURES];
            meteorDamaged = new Texture[METEOR_TEXTURES];

            for(int i = 0; i < METEOR_TEXTURES; i++)
            {
                meteorFull[i] = screen.getGame().getTexture("meteor" + (i + 1));
                meteorDamaged[i] = screen.getGame().getTexture("meteor" + (i + 1) + "_damaged");
            }

            firstInit = true;
        }

        texture = GameScreen.random.nextInt(METEOR_TEXTURES);
        radius = Scale.getScaledSizeX(GameScreen.random.nextInt(50)+50);
        health = GameScreen.random.nextInt(90)+10;
        x = GameScreen.random.nextInt(400)+SpaceGame.VIEWPORTWIDTH+200;
        y = GameScreen.random.nextInt(SpaceGame.VIEWPORTHEIGHT-(int)radius);
        box = new Circle();
        box.setX(x+radius);
        box.setY(y+radius);
        box.setRadius(radius);
        speed = GameScreen.random.nextInt(12)+5;
        saveSpeed = speed;
        rotateSpeed = GameScreen.random.nextInt(2);
        damage = (int) radius / 6;

        meteorsprite = new Sprite(health < 30 ? meteorDamaged[texture] : meteorFull[texture]);
        meteorsprite.setSize(radius * 2, radius * 2);
        meteorsprite.setOrigin(radius, radius);
    }

    public void draw(SpriteBatch batch)
    {
        meteorsprite.draw(batch);
    }

    public void update()
    {
        x -= speed;
        box.setX(x+radius);
        box.setY(y+radius);

        meteorsprite.rotate(rotateSpeed);
        meteorsprite.setPosition(x, y);
    }

    public void updateTexture()
    {
        meteorsprite.setTexture(health < 30 ? meteorDamaged[texture] : meteorFull[texture]);
    }

    public static void dispose()
    {
        for(int i = 0; i < METEOR_TEXTURES; i++)
        {
            meteorFull[i].dispose();
            meteorDamaged[i].dispose();
        }
    }

    public void reverseSpeed()
    {
        if(speed==0)
        {
            speed=saveSpeed;
        }
        else
        {
            speed=0;
        }
    }
}

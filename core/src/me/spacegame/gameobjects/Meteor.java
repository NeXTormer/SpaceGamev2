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
    public boolean divided;

    public long lastTimeHit = 0;
    public Circle box;
    public Sprite meteorsprite;
    public int speed;
    public int speedy;
    private int saveSpeed;
    public int rotateSpeed;
    public int texture;

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

        radius = GameScreen.random.nextInt(50)+50;
        damage = (int) radius / 6;
        radius = Scale.getScaledSizeX(radius);
        health = GameScreen.random.nextInt(90)+10;
        x = GameScreen.random.nextInt(400)+SpaceGame.VIEWPORTWIDTH+200;
        y = GameScreen.random.nextInt(SpaceGame.VIEWPORTHEIGHT-(int)radius);
        box = new Circle();
        box.setX(x+radius);
        box.setY(y+radius);
        box.setRadius(radius);
        speed = (int) Scale.getScaledSizeX(GameScreen.random.nextInt(8)+5); // 8 instead of 12
        speedy = 0;
        saveSpeed = speed;
        rotateSpeed = GameScreen.random.nextInt(2);
        texture = GameScreen.random.nextInt(METEOR_TEXTURES);

        if(GameScreen.random.nextInt(8)==0)
        {
            divided = true;
        }
        else
        {
            divided = false;
        }

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
        y -= speedy;
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

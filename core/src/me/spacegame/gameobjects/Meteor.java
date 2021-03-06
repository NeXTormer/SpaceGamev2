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

public class Meteor
{
    private static Texture[] meteorFull;
    private static Texture[] meteorDamaged;
    private static final int METEOR_TEXTURES = 6;

    static
    {
        meteorFull = new Texture[METEOR_TEXTURES];
        meteorDamaged = new Texture[METEOR_TEXTURES];

        for(int i = 0; i < METEOR_TEXTURES; i++)
        {
            meteorFull[i] = SpaceGame.getInstance().getTexture("meteor" + (i + 1));
            meteorDamaged[i] = SpaceGame.getInstance().getTexture("meteor" + (i + 1) + "_damaged");
        }
    }

    public float x;
    public float y;
    public float radius;
    public int damage;
    public long lastTimeHit = 0;
    public Circle box;
    public int health;
    public int speed;
    public int rotateSpeed;

    private boolean divided;
    private Sprite meteorsprite;
    private int speedy;
    private int saveSpeed;
    private int texture;

    private GameScreen gameScreen;

    public Meteor(GameScreen screen)
    {
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

        this.gameScreen = screen;
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

    public void divide()
    {
        if(divided)
        {
            Meteor m1 = new Meteor(gameScreen);
            m1.x = x;
            m1.y = y;
            m1.radius = radius;
            m1.divided = false;
            m1.speedy = speed;
            m1.texture = texture;
            m1.meteorsprite = new Sprite(meteorsprite);
            gameScreen.meteors.add(m1);
            Meteor m2 = new Meteor(gameScreen);
            m2.x = x;
            m2.y = y;
            m2.divided = false;
            m2.speedy = -speed;
            m2.radius = radius;
            m2.texture = texture;
            m2.meteorsprite = new Sprite(meteorsprite);
            gameScreen.meteors.add(m2);
        }
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
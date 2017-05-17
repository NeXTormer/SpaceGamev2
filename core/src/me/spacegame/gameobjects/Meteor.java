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

/**
 * Created by Michi on 10.05.2017.
 */

public class Meteor {


    public float x;
    public float radius;
    public int damage;
    public int health;
    public long lastTimeHit = 0;
    public Circle box;

    private static Texture mtfull;
    private static Texture mthalf;
    private Sprite meteorsprite;
    private int speed;
    private float y;
    private int rotateSpeed;
    private static Random random = new Random();

    static
    {
        mtfull = new Texture(Gdx.files.internal("gameobjects/Meteor_01.png"));
        mthalf = new Texture(Gdx.files.internal("gameobjects/Meteor_02.png"));
    }

    public Meteor()
    {
        radius = random.nextInt(50)+50;
        health = random.nextInt(90)+10;
        x = random.nextInt(400)+SpaceGame.VIEWPORTWIDTH;
        y = random.nextInt(SpaceGame.VIEWPORTHEIGHT-(int)radius);
        box = new Circle();
        box.setX(x+radius);
        box.setY(y+radius);
        box.setRadius(radius);
        speed = random.nextInt(12)+5;
        rotateSpeed = random.nextInt(2);
        damage = (int) radius / 2;

        meteorsprite = new Sprite(health < 30 ? mthalf : mtfull);
        meteorsprite.setSize(radius * 2, radius * 2);
        meteorsprite.setOrigin(radius, radius);
    }

    public void render(float delta, SpriteBatch batch)
    {


        x -= speed;
        box.setX(x+radius);
        box.setY(y+radius);

        meteorsprite.rotate(rotateSpeed);
        meteorsprite.setPosition(x, y);

        meteorsprite.draw(batch);

    }

    public void updateTexture()
    {
        meteorsprite.setTexture(health < 30 ? mthalf : mtfull);
    }

    public static void dispose()
    {
        mthalf.dispose();
        mtfull.dispose();
    }
}

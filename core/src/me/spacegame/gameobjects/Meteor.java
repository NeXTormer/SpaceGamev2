package me.spacegame.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

import java.util.Random;

import me.spacegame.SpaceGame;

/**
 * Created by Michi on 10.05.2017.
 */

public class Meteor {


    private Texture mt;
    private int speed;
    private float x;
    private float y;
    private float radius;
    private int rotateSpeed;
    private int health = 100;
    private Circle box;
    private static Random random = new Random();

    public Meteor()
    {
        radius = random.nextInt(50)+50;
        mt = new Texture(Gdx.files.internal("Meteor_01.png"));
        x = random.nextInt(400)+SpaceGame.VIEWPORTWIDTH;
        y = random.nextInt(SpaceGame.VIEWPORTHEIGHT-(int)radius);
        box = new Circle(x, y, radius);
        speed = random.nextInt(15)+5;
        rotateSpeed = random.nextInt(5);
    }

    public void render(float delta, SpriteBatch batch)
    {

        x -= speed;
        batch.draw(mt, x, y, radius*2, radius*2);

    }

    public void dispose()
    {
        mt.dispose();
    }
}

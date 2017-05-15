package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;

/**
 * Created by Michi on 15.05.2017.
 */

public class Rocket {

    private float x;
    private float y;
    private int width;
    private int height;
    private Texture texture;
    private Circle box;
    private int speed;

    public Rocket(float x, float y)
    {
        this.y = y;
        this.x = x;
        speed = 5;
        width = 30;
        height = 20;
        texture = new Texture(Gdx.files.internal("gameobjects/Rocket_01.png"));
        box = new Circle(x, y, height);
    }


    public void render(float delta, SpriteBatch batch)
    {
        x+=speed;
        box.setX(x);
        batch.draw(texture, x, y, width, height);
    }

    public void dispose()
    {
        texture.dispose();
    }
}

package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

/**
 * Created by Michi on 15.05.2017.
 */

public class Rocket {

    public float x;
    private float y;
    private int width;
    private int height;
    private Texture texture;
    public Rectangle box;
    private int speed;

    public Rocket(Player p)
    {
        speed = 18;
        width = 50;
        height = 40;
        this.y = p.y+(p.height/2)-(height/2);
        this.x = p.x+p.width;
        texture = new Texture(Gdx.files.internal("gameobjects/Rocket_01.png"));
        box = new Rectangle(x-5, y, width, height);
    }


    public void render(float delta, SpriteBatch batch)
    {
        x+=speed;
        box.setX(x-5);
        batch.draw(texture, x, y, width, height);
    }

    public void dispose()
    {
        texture.dispose();
    }
}

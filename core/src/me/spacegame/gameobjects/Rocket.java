package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import me.spacegame.SpaceGame;
import me.spacegame.util.Scale;

/**
 * Created by Michi on 15.05.2017.
 */

public class Rocket {

    private static Texture normalRocketTexture;
    private static Texture controlRocketTexture;
    static
    {
        normalRocketTexture = SpaceGame.getInstance().getTexture("rocket1");
        controlRocketTexture = new Texture(Gdx.files.internal("gameobjects/ControlRocket.png"));
    }


    private Texture texture;
    public float x;
    public float y;
    private int width;
    private int height;
    public Rectangle box;
    public int speed;
    public int damage;

    public Rocket(Player p)
    {
        texture = normalRocketTexture;
        speed = (int) Scale.getScaledSizeX(18);
        width = (int) Scale.getScaledSizeX(50);
        height = (int) Scale.getScaledSizeX(40);
        this.y = p.y+(p.height/2)-(height/2);
        this.x = p.x+p.width;
        damage = 30;

        box = new Rectangle(x-5, y, width, height);
    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(texture, x, y, width, height);
    }

    public void setNormalRocketTexture()
    {
        texture = normalRocketTexture;
    }

    public void setControlRocketTexture()
    {
        texture = controlRocketTexture;
    }

    public void update(float delta)
    {
        x += Math.round(speed * delta);
        box.setX(x-5);
        box.setY(y);
    }

    public static void dispose()
    {
        normalRocketTexture.dispose();
        controlRocketTexture.dispose();
    }
}

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

public class EnemyRocket {

    public static Texture texture;

    static
    {
        texture = SpaceGame.getInstance().getTexture("enemyrocket1");
    }

    public float x;
    public float y;
    private int width;
    private int height;
    public Rectangle box;
    public int speed;
    public int damage;

    public boolean hasHitPlayer = false;

    public EnemyRocket(Enemy p)
    {
        damage=10;
        speed = (int) Scale.getScaledSizeX(40);
        width = (int) Scale.getScaledSizeX(50);
        height = (int) Scale.getScaledSizeX(40);
        this.y = p.enemyY+(p.enemyHeight/2)-(height/2);
        this.x = p.enemyX-(width+2);

        box = new Rectangle(x-5, y, width, height);
    }


    public void draw(SpriteBatch batch)
    {
        batch.draw(texture, x, y, width, height);
    }

    public void update()
    {
        x-=speed;
        box.setX(x+5);
        box.setY(y);
    }

    public static void dispose()
    {
        texture.dispose();
    }
}

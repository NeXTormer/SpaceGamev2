package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import me.spacegame.SpaceGame;

/**
 * Created by Michi on 17.05.2017.
 */

public class Enemy {

    public float enemyX;
    public float enemyY;
    public int enemyWidth;
    public int enemyHeight;
    public Rectangle box;
    public int health = 100;

    private int baseSpeed;
    private static Texture enemyTexture;
    private static Random random = new Random();

    private static Texture warningTexture;
    public float warningX;
    public float warningY;
    public float warningWidth;
    public float warningHeight;

    static
    {

    }

    public Enemy()
    {
        enemyX = random.nextInt(1000)+ SpaceGame.VIEWPORTWIDTH+2000;
        enemyWidth=150;
        enemyHeight=150;
        enemyY = random.nextInt(SpaceGame.VIEWPORTHEIGHT-enemyHeight);
        box = new Rectangle(enemyX, enemyY, enemyWidth, enemyHeight);
        baseSpeed = 32
        ;

        enemyTexture = new Texture(Gdx.files.internal("gameobjects/SpaceShip_02.png"));
        warningTexture = new Texture(Gdx.files.internal("gameobjects/Warning_01.png"));

        warningX = SpaceGame.VIEWPORTWIDTH-50;
        warningY = enemyY;
        warningHeight = enemyHeight;
        warningWidth = 40;
    }

    public void render(float delta, SpriteBatch batch)
    {
        enemyX-=baseSpeed;
        box.setX(enemyX);


        batch.draw(enemyTexture, enemyX, enemyY, enemyWidth, enemyHeight);
        if(enemyX>SpaceGame.VIEWPORTWIDTH)
        {
            batch.draw(warningTexture, warningX, warningY, warningWidth, warningHeight);
        }

    }


    public static void dispose()
    {
        enemyTexture.dispose();
        warningTexture.dispose();
    }
}

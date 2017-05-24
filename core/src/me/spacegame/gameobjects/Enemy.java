package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.ui.List;

import java.util.ArrayList;
import java.util.Random;

import me.spacegame.SpaceGame;
import me.spacegame.animations.ExclaimationPoint;

/**
 * Created by Michi on 17.05.2017.
 */

public class Enemy {

    public float enemyX;
    public float enemyY;
    public int enemyWidth;
    public int enemyHeight;
    public Rectangle box;
    public int health;
    public int damage;

    private int baseSpeed;
    private static Texture enemyTexture;
    private static Random random = new Random();
    private int rocket1;
    private int rocket2;

    public float warningX;
    public float warningY;
    public float warningWidth;
    public float warningHeight;

    private ArrayList<EnemyRocket> rockets;

    ExclaimationPoint ep;

    static
    {
        enemyTexture = new Texture(Gdx.files.internal("gameobjects/EnemyShip_02.png"));
    }

    public Enemy()
    {
        enemyX = random.nextInt(1000)+ SpaceGame.VIEWPORTWIDTH+2000;
        enemyWidth=150;
        enemyHeight=150;
        enemyY = random.nextInt(SpaceGame.VIEWPORTHEIGHT-enemyHeight);
        box = new Rectangle(enemyX, enemyY, enemyWidth, enemyHeight);
        baseSpeed = 22;
        damage=40;
        health = 100;



        warningX = SpaceGame.VIEWPORTWIDTH-50;
        warningY = enemyY;
        warningHeight = enemyHeight;
        warningWidth = 40;

        ep = new ExclaimationPoint((int) warningX, (int) warningY);
        rockets = new ArrayList<EnemyRocket>();
        rocket1 = random.nextInt(SpaceGame.VIEWPORTWIDTH-200)+200;
        rocket2 = random.nextInt(SpaceGame.VIEWPORTWIDTH-400)+400;
    }

    public void render(float delta, SpriteBatch batch)
    {
        enemyX-=baseSpeed;
        box.setX(enemyX);


        batch.draw(enemyTexture, enemyX, enemyY, enemyWidth, enemyHeight);
        if(enemyX>SpaceGame.VIEWPORTWIDTH)
        {
            ep.draw(delta, batch);
        }

        if(enemyX>=1800 && enemyX <=1850)
        {
            rockets.add(new EnemyRocket(this));
        }

        if(enemyX>=rocket1 && enemyX <=rocket1+50)
        {
            rockets.add(new EnemyRocket(this));
        }

        if(enemyX>=rocket2 && enemyX <=rocket2+50)
        {
            rockets.add(new EnemyRocket(this));
        }

        for(EnemyRocket er : rockets)
        {
            er.render(delta, batch);
        }

        for(int i = 0; i<rockets.size(); i++)
        {
            if(rockets.get(i).x<0)
            {
                rockets.remove(rockets.get(i));
            }
        }


    }


    public static void dispose()
    {
        enemyTexture.dispose();

    }

    public ArrayList<EnemyRocket> getRockets()
    {
        return rockets;
    }
}

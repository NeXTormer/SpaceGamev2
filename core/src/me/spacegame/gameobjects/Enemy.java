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
import me.spacegame.screens.GameScreen;

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
    public Player player;
    public long shootTime;

    public int type;
    //type = 0 --> Enemy from Right
    //type = 1 --> Enemy from Left

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

    }

    public Enemy(int type)
    {
        this.type = type;
        enemyX = random.nextInt(1000)+ SpaceGame.VIEWPORTWIDTH+2000;
        enemyTexture = new Texture(Gdx.files.internal("gameobjects/EnemyShip_02.png"));
        warningX = SpaceGame.VIEWPORTWIDTH-50;

        enemyWidth=150;
        enemyHeight=150;
        enemyY = random.nextInt(SpaceGame.VIEWPORTHEIGHT-enemyHeight);
        box = new Rectangle(enemyX, enemyY, enemyWidth, enemyHeight);
        baseSpeed = 22;
        damage=40;
        health = 100;


        warningY = enemyY;
        warningHeight = enemyHeight;
        warningWidth = 40;

        ep = new ExclaimationPoint((int) warningX, (int) warningY);
        rockets = new ArrayList<EnemyRocket>();
        rocket1 = random.nextInt(SpaceGame.VIEWPORTWIDTH-200)+200;
        rocket2 = random.nextInt(SpaceGame.VIEWPORTWIDTH-400)+400;
    }

    public Enemy(int type, Player p)
    {
        enemyX = (random.nextInt(1000)*(-1))-1000;
        enemyTexture = new Texture(Gdx.files.internal("gameobjects/EnemyShip_02.png"));
        warningX = 50;
        this.type=type;
        player = p;
        shootTime = System.currentTimeMillis();

        enemyWidth=150;
        enemyHeight=150;
        enemyY = random.nextInt(SpaceGame.VIEWPORTHEIGHT-enemyHeight);
        box = new Rectangle(enemyX, enemyY, enemyWidth, enemyHeight);
        baseSpeed = 22;
        damage=40;
        health = 100;


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
        if(type==0)
        {
            enemyX-=baseSpeed;
        }
        if(type==1)
        {
            if(enemyX<=100)
            {
                enemyX+=(baseSpeed-10);
            }
            if(player.y<enemyY)
            {
                enemyY-=5;
            }
            if(player.y>enemyY)
            {
                enemyY+=5;
            }
            if((System.currentTimeMillis()-shootTime) > 2000)
            {
                shootTime = System.currentTimeMillis();
                EnemyRocket r1 = new EnemyRocket(this);
                r1.speed*=-1;
                r1.x=enemyX+enemyWidth;
                r1.y=enemyY+(enemyHeight*2/7);
                rockets.add(r1);
                EnemyRocket r2 = new EnemyRocket(this);
                r2.speed*=-1;
                r2.x=enemyX+enemyWidth;
                r2.y=enemyY+(enemyHeight*5/7);
                rockets.add(r2);
            }
        }

        box.setX(enemyX);
        box.setY(enemyY);

        batch.draw(enemyTexture, enemyX, enemyY, enemyWidth, enemyHeight);
        if(enemyX>SpaceGame.VIEWPORTWIDTH && type==0)
        {
            ep.draw(delta, batch);
        }

        if(enemyX<0 && type==1)
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
            if(rockets.get(i).x<0 && type==0)
            {
                rockets.remove(rockets.get(i));
            }
            else if(rockets.get(i).x>SpaceGame.VIEWPORTWIDTH && type==1)
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

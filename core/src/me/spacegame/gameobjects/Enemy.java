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
import me.spacegame.util.Scale;

/**
 * Created by Michi on 17.05.2017.
 */

public class Enemy {

    private GameScreen gameScreen;

    public float enemyX;
    public float enemyY;
    public int enemyWidth;
    public int enemyHeight;
    public Rectangle box;
    public int health;
    public int damage;
    public Player player;
    public long shootTime;

    public long lastTimeHit;

    public int type;
    //type = 0 --> Enemy from Right
    //type = 1 --> Enemy from Left

    private int baseSpeed;
    private Texture enemyTexture;
    private static Random random = new Random();
    private int rocket1;
    private int rocket2;

    public float warningX;
    public float warningY;
    public float warningWidth;
    public float warningHeight;


    private ArrayList<EnemyRocket> rockets;

    public ExclaimationPoint ep;


    public Enemy(int type, GameScreen screen)
    {
        gameScreen = screen;
        this.type = type;
        enemyX = random.nextInt(1000)+ SpaceGame.VIEWPORTWIDTH+2000;
        warningX = SpaceGame.VIEWPORTWIDTH-50;

        enemyWidth = (int) Scale.getScaledSizeX(150);
        enemyHeight = (int) Scale.getScaledSizeY(150);
        enemyY = random.nextInt(SpaceGame.VIEWPORTHEIGHT-enemyHeight);
        box = new Rectangle(enemyX, enemyY, enemyWidth, enemyHeight);
        baseSpeed = (int) Scale.getScaledSizeX(22);
        damage=20;
        health = 100;
        enemyTexture = screen.getGame().getTexture("enemy1");


        warningY = enemyY;
        warningHeight = enemyHeight;
        warningWidth = 40;

        ep = new ExclaimationPoint((int) warningX, (int) warningY, screen);
        rockets = new ArrayList<EnemyRocket>();
        rocket1 = random.nextInt(SpaceGame.VIEWPORTWIDTH-200)+200;
        rocket2 = random.nextInt(SpaceGame.VIEWPORTWIDTH-400)+400;
    }

    public Enemy(int type, Player p, GameScreen screen)
    {
        gameScreen = screen;
        enemyX = (random.nextInt(1000)*(-1))-1000;
        enemyTexture = screen.getGame().getTexture("enemy2");
        warningX = 50;
        this.type=type;
        player = p;
        shootTime = System.currentTimeMillis();

        enemyWidth = (int) Scale.getScaledSizeX(150);
        enemyHeight = (int) Scale.getScaledSizeY(150);
        enemyY = p.y;
        box = new Rectangle(enemyX, enemyY, enemyWidth, enemyHeight);
        baseSpeed = 22;
        damage=40;
        health = 100;


        warningY = enemyY;
        warningHeight = enemyHeight;
        warningWidth = 40;

        ep = new ExclaimationPoint((int) warningX, (int) warningY, screen);
        rockets = new ArrayList<EnemyRocket>();
        rocket1 = random.nextInt(SpaceGame.VIEWPORTWIDTH-200)+200;
        rocket2 = random.nextInt(SpaceGame.VIEWPORTWIDTH-400)+400;

    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(enemyTexture, enemyX, enemyY, enemyWidth, enemyHeight);
        if(enemyX>SpaceGame.VIEWPORTWIDTH && type==0)
        {
            ep.draw(batch);
        }

        if(enemyX<0 && type==1)
        {
            ep.draw(batch);
        }

        for(EnemyRocket er : rockets)
        {
            er.draw(batch);
        }
    }

    public void update()
    {
        ep.update();
        ep.setPosition( (int) warningX, (int) enemyY);

        if(type==0)
        {
            enemyX-=baseSpeed;
        }
        if(type==1)
        {
            if(enemyX<=150)
            {
                enemyX+=(baseSpeed-10);
            }
            if(player.y<enemyY)
            {
                enemyY-=3;
            }
            if(player.y>enemyY)
            {
                enemyY+=3;
            }
            if((System.currentTimeMillis()-shootTime) > 4000)
            {
                shootTime = System.currentTimeMillis();

                //bot rocket
                EnemyRocket r1 = new EnemyRocket(this);
                r1.texture = gameScreen.getGame().getTexture("enemyrocket2");
                r1.speed*=-1;
                r1.x=enemyX+(enemyWidth/4*3);
                r1.y=enemyY+(enemyHeight*2/28);
                rockets.add(r1);

                //top rocket
                EnemyRocket r2 = new EnemyRocket(this);
                r2.texture = gameScreen.getGame().getTexture("enemyrocket2");
                r2.speed*=-1;
                r2.x=enemyX+(enemyWidth/4*3);
                r2.y=enemyY+(enemyHeight*20/28);
                rockets.add(r2);
            }
        }

        box.setX(enemyX);
        box.setY(enemyY);

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

        if(enemyX>=Scale.getScaledSizeX(1800) && enemyX <= Scale.getScaledSizeX(1850))
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

        for(EnemyRocket rocket : rockets)
        {
            rocket.update();
        }

    }

    public GameScreen getGameScreen()
    {
        return gameScreen;
    }

    public ArrayList<EnemyRocket> getRockets()
    {
        return rockets;
    }
}

package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
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
    //type = 2 --> Enemy from right with sinus
    //type = 3 --> Enemy from right to left with sinus

    private int baseSpeed;
    private Texture enemyTexture;
    private static Random random = new Random();
    private int rocket1;
    private int rocket2;

    private float warningX;
    private float warningY;
    private float warningWidth;
    private float warningHeight;

    private float sinCount;
    private double shootTime2;
    private double shootSpawn = 500;
    private boolean started = false;

    private long soundID = 0;
    private Sound sound;


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
        sound = gameScreen.getGame().getSound("warningsound");
        soundID = sound.play(gameScreen.game.soundVolume);
        sound.setLooping(soundID, true);

        ep = new ExclaimationPoint((int) warningX, (int) warningY, screen);
        rockets = new ArrayList<EnemyRocket>();
        rocket1 = random.nextInt(SpaceGame.VIEWPORTWIDTH-200)+200;
        rocket2 = random.nextInt(SpaceGame.VIEWPORTWIDTH-400)+400;
    }

    public Enemy(int type, Player p, GameScreen screen)
    {
        gameScreen = screen;
        enemyX = (random.nextInt(1000)*(-1))-1500;
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
        damage=15;
        health = 100;


        warningY = enemyY;
        warningHeight = enemyHeight;
        warningWidth = Scale.getScaledSizeX(40);
        sound = gameScreen.getGame().getSound("warningsound");
        soundID = sound.play(gameScreen.game.soundVolume);
        sound.setLooping(soundID, true);

        ep = new ExclaimationPoint((int) warningX, (int) warningY, screen);
        rockets = new ArrayList<EnemyRocket>();
        rocket1 = random.nextInt(SpaceGame.VIEWPORTWIDTH-200)+200;
        rocket2 = random.nextInt(SpaceGame.VIEWPORTWIDTH-400)+400;

    }

    public void draw(SpriteBatch batch)
    {
        batch.draw(enemyTexture, enemyX, enemyY, enemyWidth, enemyHeight);
        if(enemyX>SpaceGame.VIEWPORTWIDTH && (type==0 || type==2 || type==3))
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

        if((type==2 || type==0 || type==3) && enemyX<Scale.getScaledSizeX(1920))
        {
            sound.stop(soundID);
        }
        if(type==1 && enemyX>0)
        {
            sound.stop(soundID);
        }

        if(type==0)
        {
            enemyX-=baseSpeed;
        }
        if(type==1)
        {
            if(enemyX<=Scale.getScaledSizeX(150))
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
                gameScreen.game.getSound("shot3sound").play(gameScreen.game.soundVolume);

                //top rocket
                EnemyRocket r2 = new EnemyRocket(this);
                r2.texture = gameScreen.getGame().getTexture("enemyrocket2");
                r2.speed*=-1;
                r2.x=enemyX+(enemyWidth/4*3);
                r2.y=enemyY+(enemyHeight*20/28);
                rockets.add(r2);
                gameScreen.game.getSound("shot3sound").play(gameScreen.game.soundVolume);
            }
        }

        if((type==3 || type==2))
        {
            if(!started)
            {
                shootTime2 = System.currentTimeMillis();
                started = true;
            }

            sinCount+=0.05;

            if(type==3)
            {
                if(enemyX>=Scale.getScaledSizeX(1600))
                {
                    enemyX-=(baseSpeed);
                }

            }
            if(type==2)
            {
                enemyX-=(baseSpeed);
            }

            double value = 300*Math.sin(sinCount);
            enemyY = (float) value+(SpaceGame.VIEWPORTHEIGHT/2);

            if((System.currentTimeMillis()-shootTime2) > shootSpawn) {
                shootTime2 = System.currentTimeMillis();
                shootSpawn = random.nextInt(1000)+1000;
                if(enemyX<SpaceGame.VIEWPORTWIDTH)
                {
                    rockets.add(new EnemyRocket(this));
                    gameScreen.game.getSound("shot3sound").play(gameScreen.game.soundVolume);
                }
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

        if(type==0)
        {
            if(enemyX>=Scale.getScaledSizeX(1800) && enemyX <= Scale.getScaledSizeX(1850))
            {
                rockets.add(new EnemyRocket(this));
                gameScreen.game.getSound("shot3sound").play(gameScreen.game.soundVolume);
            }

            if(enemyX>=rocket1 && enemyX <=rocket1+50)
            {
                rockets.add(new EnemyRocket(this));
                gameScreen.game.getSound("shot3sound").play(gameScreen.game.soundVolume);
            }

            if(enemyX>=rocket2 && enemyX <=rocket2+50)
            {
                rockets.add(new EnemyRocket(this));
                gameScreen.game.getSound("shot3sound").play(gameScreen.game.soundVolume);
            }
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

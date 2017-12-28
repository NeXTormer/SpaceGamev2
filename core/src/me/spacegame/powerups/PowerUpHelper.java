package me.spacegame.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;

import me.spacegame.SpaceGame;
import me.spacegame.animations.Explosion;
import me.spacegame.gameobjects.Enemy;
import me.spacegame.gameobjects.EnemyRocket;
import me.spacegame.gameobjects.Meteor;
import me.spacegame.gameobjects.Player;
import me.spacegame.gameobjects.Rocket;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 26.06.2017.
 */



public class PowerUpHelper extends PowerUp {

    private Player helper;
    private double value;
    private float sinCount;
    private double shootTime;

    public PowerUpHelper(Player player, GameScreen gameScreen) {
        super(player, gameScreen);
        helper = new Player(gameScreen);
        helper.x = -500;
        helper.health = 3000;
        helper.texture = gameScreen.getGame().getTexture("helpership");

        started = false;
        texture = gameScreen.getGame().getTexture("pwupHelperIcon");
        //texture = new Texture(Gdx.files.internal("gameobjects/SpaceShip_02.png"));
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean update()
    {
        if(!started)
        {
            shootTime = System.currentTimeMillis();
            started = true;
        }

        sinCount+=0.05;

        if(helper.x<=500)
        {
            helper.x+=(helper.baseSpeed);
        }

        value = 300*Math.sin(sinCount);
        helper.y = (float) value+(SpaceGame.VIEWPORTHEIGHT/2);

        helper.box.setPosition(helper.x, helper.y);

        if((System.currentTimeMillis()-shootTime) > 500) {
            shootTime = System.currentTimeMillis();
            gameScreen.rockets.add(new Rocket(helper));
        }

        //Meteor - Helper Collision
        for (int i = 0; i < gameScreen.meteors.size(); i++) {
            if (Intersector.overlaps(gameScreen.meteors.get(i).box, helper.box))
            {
                helper.health-=40;
            }
        }

        //Enemy - Helper Collision
        for(Enemy e : gameScreen.enemies) {
            for (EnemyRocket er : e.getRockets()) {
                if (Intersector.overlaps(er.box, helper.box)) {
                    helper.health -= 40;
                }
            }
            if (Intersector.overlaps(e.box, helper.box)) {
                if ((System.currentTimeMillis() - e.lastTimeHit) > 1200) {
                    helper.health-=40;
                }
            }
        }


        if(helper.health<=0)
        {
            gameScreen.explosions.add(new Explosion((int) helper.x - 70, (int) (helper.y - 70), true));
            return false;
        }

        return true;
    }

    public void draw(SpriteBatch batch)
    {
        helper.draw(batch);
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void dispose() {

    }
}

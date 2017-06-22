package me.spacegame.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.gameobjects.Player;
import me.spacegame.gameobjects.Rocket;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 07.06.2017.
 */

public class PowerUpRapidFire extends PowerUp {

    private double duration;
    private double durationStart;
    private double rocketTimer;
    private double rocketSpawn;
    private double x;
    private double y;

    static
    {
        texture = new Texture(Gdx.files.internal("gameobjects/SpaceShip_01.png"));
    }


    public PowerUpRapidFire(Player p, GameScreen gameScreen)
    {
        super(p, gameScreen);
        rocketTimer = System.currentTimeMillis();
        rocketSpawn = 70;
        duration = 5000;
        durationStart = System.currentTimeMillis();
    }


    @Override
    public boolean render(float delta, SpriteBatch batch)
    {
        if((System.currentTimeMillis()-durationStart)<duration)
        {
            if((System.currentTimeMillis()-rocketTimer)>rocketSpawn)
            {
                gameScreen.rockets.add(new Rocket(player));
                rocketTimer=System.currentTimeMillis();
            }
            return true;
        }
        else
        {
            return false;
        }

    }

    @Override
    public void dispose() {

    }

    @Override
    public void start()
    {

    }

    @Override
    public void stop() {

    }

}

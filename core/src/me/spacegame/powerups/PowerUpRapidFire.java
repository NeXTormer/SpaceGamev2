package me.spacegame.powerups;

import com.badlogic.gdx.Screen;
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


    public PowerUpRapidFire(Player p, GameScreen gameScreen)
    {
        super(p, gameScreen);
        rocketTimer = System.currentTimeMillis();
        rocketSpawn = 100;
        duration = 5000;
        durationStart = System.currentTimeMillis();
    }


    @Override
    public boolean render(float delta, SpriteBatch batch)
    {
        if((durationStart-System.currentTimeMillis())<duration)
        {
            if((rocketTimer-System.currentTimeMillis())>rocketSpawn)
            {
                gameScreen.rockets.add(new Rocket(player));
                rocketSpawn=System.currentTimeMillis();
            }
            return true;
        }
        return false;

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

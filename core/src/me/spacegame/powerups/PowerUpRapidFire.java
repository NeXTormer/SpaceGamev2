package me.spacegame.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
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


    private double rocketTimer;
    private double rocketSpawn;
    private double x;
    private double y;
    private boolean started;

    private long soundID = 0;
    private Sound sound;


    public PowerUpRapidFire(Player p, GameScreen gameScreen)
    {
        super(p, gameScreen);
        rocketTimer = System.currentTimeMillis();
        rocketSpawn = 70;
        duration = 5000;
        started = false;
        durationStart = System.currentTimeMillis();
        timer = 0;
        texture = gameScreen.getGame().getTexture("pwupRapidFireIcon");
    }

    @Override
    public void draw(SpriteBatch batch) { }

    @Override
    public boolean update()
    {
        if(!started)
        {
            started = true;
            rocketTimer = System.currentTimeMillis();
            durationStart = System.currentTimeMillis();
        }
        timer = System.currentTimeMillis()-durationStart;
        if((timer)<duration)
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
            sound.stop(soundID);
            return false;
        }

    }

    @Override
    public boolean isFinished()
    {
        return (System.currentTimeMillis()-durationStart)<duration;
    }

    @Override
    public void dispose() {

    }

    @Override
    public float getCooldown() {
        return (float) (timer/duration)*100.0f;
    }

    @Override
    public void start()
    {
        sound = gameScreen.getGame().getSound("shot2csound");
        soundID = sound.play(gameScreen.game.soundVolume);
        sound.setLooping(soundID, true);
    }

    @Override
    public void stop() {

    }

}

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

public class PowerUpControl extends PowerUp
{

    private boolean started;
    private float height;
    private GameScreen gameScreen;

    public PowerUpControl(Player p, GameScreen gameScreen)
    {
        super(p, gameScreen);
        height = 40;
        duration = 5000;
        durationStart = System.currentTimeMillis();
        started=false;
        timer = 0;
        texture = new Texture(Gdx.files.internal("gameobjects/ControlRocketIcon.png"));
        this.gameScreen = gameScreen;
    }


    @Override
    public boolean update()
    {
        if(!started)
        {
            started = true;
        }
        timer = System.currentTimeMillis() - durationStart;
        if ((timer) < duration)
        {
            for (int i = 0; i < gameScreen.rockets.size(); i++)
            {
                gameScreen.rockets.get(i).setControlRocketTexture();
                gameScreen.rockets.get(i).damage = 100;
                gameScreen.rockets.get(i).y = gameScreen.player.y+(gameScreen.player.height/2)-(height/2);
            }
            return true;
        }

        for(int i = 0; i < gameScreen.rockets.size(); i++)
        {
            gameScreen.rockets.get(i).setNormalRocketTexture();
        }
        return false;
    }

    @Override
    public void draw(SpriteBatch batch) {}

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void dispose() { }

    @Override
    public float getCooldown()
    {
        return (float) (timer/duration)*100.0f;
    }

    @Override
    public void start() { }

    @Override
    public void stop()
    {
       // sound.stop(soundID);
        timer = duration+=1;
        durationStart= -duration;
    }
}

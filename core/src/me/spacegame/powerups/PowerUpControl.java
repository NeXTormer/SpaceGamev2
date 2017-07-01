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

public class PowerUpControl extends PowerUp {

    private double rocketTimer;
    private double rocketSpawn;
    private boolean started;
    private float x;
    private float y;
    private float width;
    private float height;
    private static Texture replaceTexture;

    static
    {
        replaceTexture = new Texture(Gdx.files.internal("gameobjects/ControlRocket.png"));
    }

    public PowerUpControl(Player p, GameScreen gameScreen) {
        super(p, gameScreen);
        width = 50;
        height = 40;
        duration = 6000;
        durationStart = System.currentTimeMillis();
        started=false;
        timer = 0;
        texture = new Texture(Gdx.files.internal("gameobjects/ControlRocketIcon.png"));
    }


    @Override
    public boolean render(float delta, SpriteBatch batch) {
        if(!started)
        {
            started = true;
            rocketTimer = System.currentTimeMillis();
            durationStart = System.currentTimeMillis();
        }
        timer = System.currentTimeMillis() - durationStart;
        if ((timer) < duration) {
            for (int i = 0; i < gameScreen.rockets.size(); i++) {
                gameScreen.rockets.get(i).texture = replaceTexture;
                gameScreen.rockets.get(i).damage = 100;
                gameScreen.rockets.get(i).y = gameScreen.player.y+(gameScreen.player.height/2)-(height/2);
            }
            return true;
        }


        Rocket.texture = new Texture(Gdx.files.internal("gameobjects/Rocket_01.png"));
        return false;

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void dispose() {

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }



}

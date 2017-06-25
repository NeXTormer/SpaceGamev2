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

    private double duration;
    private double durationStart;
    private double rocketTimer;
    private double rocketSpawn;
    private float x;
    private float y;
    private float width;
    private float height;
    private Texture replaceTexture;

    public PowerUpControl(Player p, GameScreen gameScreen) {
        super(p, gameScreen);
        width = 50;
        height = 40;
        duration = 10000;
        durationStart = System.currentTimeMillis();
        texture = new Texture(Gdx.files.internal("gameobjects/ControlRocketIcon.png"));
        replaceTexture = new Texture(Gdx.files.internal("gameobjects/ControlRocket.png"));
    }


    @Override
    public boolean render(float delta, SpriteBatch batch) {
        if ((System.currentTimeMillis() - durationStart) < duration) {
            for (int i = 0; i < gameScreen.rockets.size(); i++) {
                gameScreen.rockets.get(i).texture = replaceTexture;
                gameScreen.rockets.get(i).damage = 100;
                gameScreen.rockets.get(i).y = gameScreen.player.y+(gameScreen.player.height/2)-(height/2);
            }
            return true;
        }
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

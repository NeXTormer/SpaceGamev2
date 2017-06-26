package me.spacegame.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.gameobjects.Player;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 26.06.2017.
 */

public class PowerUpHealth extends PowerUp {
    private double health;
    private boolean healed;

    public PowerUpHealth(Player player, GameScreen gameScreen) {
        super(player, gameScreen);
        health =  100;
        healed = false;
        texture = new Texture(Gdx.files.internal("gameobjects/HealthIcon.png"));

    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean render(float delta, SpriteBatch batch) {
        if(!healed)
        {
            healed=true;
            player.health+=health;
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
}

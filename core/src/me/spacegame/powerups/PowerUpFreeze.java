package me.spacegame.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Cursor;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.gameobjects.Player;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 26.06.2017.
 */

public class PowerUpFreeze extends PowerUp {

    public PowerUpFreeze(Player player, GameScreen gameScreen) {
        super(player, gameScreen);
        duration = 7000;
        started = false;
        texture = new Texture(Gdx.files.internal("gameobjects/SpaceShip_02.png"));
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean update() {
        if(!started)
        {
            started = true;
            durationStart = System.currentTimeMillis();
            for(int i = 0; i<gameScreen.meteors.size(); i++)
            {
                gameScreen.meteors.get(i).reverseSpeed();
            }
        }
        if((System.currentTimeMillis()-durationStart)>duration)
        {
            for(int i = 0; i<gameScreen.meteors.size(); i++)
            {
                gameScreen.meteors.get(i).reverseSpeed();
            }
            return false;
        }
        return true;

    }

    @Override
    public void draw(SpriteBatch batch)
    {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void dispose() {

    }
}

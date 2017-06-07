package me.spacegame.powerups;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.gameobjects.Player;

/**
 * Created by Michi on 07.06.2017.
 */

public class PowerUpRapidFire extends PowerUp {

    private Player player;
    private double duration;
    private double x;
    private double y;


    public PowerUpRapidFire(Player p)
    {
        super(p);
    }


    @Override
    public void render(float delta, SpriteBatch batch)
    {

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

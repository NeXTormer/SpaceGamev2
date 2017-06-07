package me.spacegame.powerups;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.gameobjects.Player;

/**
 * Created by Michi on 07.06.2017.
 */

public abstract class PowerUp {
    protected Player player;

    public PowerUp(Player player)
    {
        this.player=player;

    }

    public abstract void start();

    public abstract void stop();

    public abstract void render(float delta, SpriteBatch batch);

    public abstract void dispose();
}

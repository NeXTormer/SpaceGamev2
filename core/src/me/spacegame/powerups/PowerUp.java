package me.spacegame.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.gameobjects.Player;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 07.06.2017.
 */

public abstract class PowerUp {
    protected Player player;
    protected GameScreen gameScreen;
    public Texture texture;
    public double duration;
    public double durationStart;
    public double timer;
    public boolean started;

    public PowerUp(Player player, GameScreen gameScreen)
    {
        this.gameScreen = gameScreen;
        this.player=player;

    }

    public abstract void start();

    public abstract void stop();

    public abstract boolean render(float delta, SpriteBatch batch);

    public abstract boolean isFinished();

    public abstract void dispose();
}

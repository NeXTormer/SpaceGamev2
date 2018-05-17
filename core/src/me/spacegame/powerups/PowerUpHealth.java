package me.spacegame.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.animations.HealthUp;
import me.spacegame.gameobjects.Player;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 26.06.2017.
 */

public class PowerUpHealth extends PowerUp {
    private double health;
    private boolean healed;
    private long soundID = 0;
    private Sound sound;
    private HealthUp hup;

    public PowerUpHealth(Player player, GameScreen gameScreen) {
        super(player, gameScreen);
        duration = 3000;
        health =  100;
        healed = false;
        texture = new Texture(Gdx.files.internal("gameobjects/HealthIcon.png"));
        hup = new HealthUp((int) player.x, (int) player.y, 300, 300, gameScreen);

    }

    @Override
    public void start()
    {

        sound = gameScreen.getGame().getSound("healthupsound");
        soundID = sound.play(gameScreen.game.soundVolume);
    }

    @Override
    public void stop() {
        timer = duration+=1;
        durationStart= -duration;

    }

    @Override
    public boolean update()
    {
        hup.update();
        if (!started)
        {
            durationStart = System.currentTimeMillis();
            started = true;
        }
        timer = System.currentTimeMillis() - durationStart;
        if ((timer) < duration)
        {
            if(!healed)
            {
                healed=true;
                //player.health+=health;
                //gameScreen.healthBar.addHealthByForce(100);
                player.health += 40;
                gameScreen.healthBar.setAbsuloteHealth(player.health);
            }
            return true;
        }
        return false;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if ((timer) < duration)
        {
            hup.draw(batch);
        }
    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void dispose() {

    }

    @Override
    public float getCooldown() {
        return (float) (timer/duration)*100.0f;
    }
}


package me.spacegame.powerups;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import me.spacegame.SpaceGame;
import me.spacegame.animations.Explosion;
import me.spacegame.gameobjects.Meteor;
import me.spacegame.gameobjects.Player;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 26.06.2017.
 */

public class PowerUpClear extends PowerUp {

    private boolean explosion;
    private int count;


    public PowerUpClear(Player player, GameScreen gameScreen) {
        super(player, gameScreen);
        explosion = false;
        texture = new Texture(Gdx.files.internal("gameobjects/ClearIcon.png"));
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean update() {
        if(!explosion)
        {
            explosion=true;
            count = gameScreen.meteors.size();
            for(int i = 0; i<count; i++)
            {
                gameScreen.explosions.add(new Explosion((int) gameScreen.meteors.get(i).x - 70, (int) (gameScreen.meteors.get(i).y - 20), true));
            }
            gameScreen.meteors.clear();
            //for(int i = 0; i<count; i++)
            //{
            //    gameScreen.meteors.add(new Meteor(gameScreen));
            //}
            for(int i = 0; i<gameScreen.enemies.size(); i++)
            {
                gameScreen.explosions.add(new Explosion((int) gameScreen.enemies.get(i).enemyX - 70, (int) (gameScreen.enemies.get(i).enemyY - 20), true));
            }
            gameScreen.enemies.clear();
        }
        SpaceGame.getInstance().getSound("explosion2sound").play(SpaceGame.getInstance().soundVolume);
        return false;

    }

    @Override
    public void draw(SpriteBatch batch) {}

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void dispose() {

    }

    @Override
    public float getCooldown() {
        return 1000;
    }
}

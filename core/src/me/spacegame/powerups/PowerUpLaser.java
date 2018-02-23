package me.spacegame.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;

import me.spacegame.animations.Laser;
import me.spacegame.gameobjects.Comet;
import me.spacegame.gameobjects.Player;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 23.02.2018.
 */

public class PowerUpLaser extends PowerUp
{
    private boolean started;
    private float x;
    private float y;
    private float width;
    private float height;
    private static Texture laserTexture;
    private Circle box;
    private Laser[] lArray;
    private int lasernumber;

    public PowerUpLaser(Player player, GameScreen gameScreen)
    {
        super(player, gameScreen);
        duration = 30000;
        texture = gameScreen.getGame().getTexture("pwupLaserIcon");
        lArray = new Laser[100];
        lasernumber = 0;

        for(int i = 0; i<lArray.length; i++)
        {
            lasernumber++;
            lArray[i] = new Laser((int) x, (int) y, gameScreen);;
            lArray[i].setPosition(player.x+x+(i*30), player.y+y);
            if(lasernumber>24)
            {
                lasernumber=4;
            }
            lArray[i].elapsedFrames=lasernumber;
        }

    }
    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public void draw(SpriteBatch batch)
    {
        if ((timer) < duration)
        {
            for(int i = 0; i<lArray.length; i++)
            {
                lArray[i].draw(batch);
            }
            //batch.draw(pacManTexture, x, y, width, height);
        }
    }

    @Override
    public boolean update()
    {
        for(int i = 0; i<lArray.length; i++)
        {
            lArray[i].setPosition((player.x+player.width-50)+x+(i*30), player.y+y+10);
            lArray[i].update();
        }
        if (!started)
        {
            durationStart = System.currentTimeMillis();
            started = true;
        }

        timer = System.currentTimeMillis() - durationStart;

        if ((timer) < duration)
        {
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
    public float getCooldown()
    {
        return (float) (timer/duration)*100.0f;
    }
}

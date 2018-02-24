package me.spacegame.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import me.spacegame.animations.Explosion;
import me.spacegame.animations.Laser;
import me.spacegame.gameobjects.Comet;
import me.spacegame.gameobjects.Meteor;
import me.spacegame.gameobjects.Player;
import me.spacegame.screens.GameScreen;
import me.spacegame.util.Scale;

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
    private Rectangle box;
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
            lArray[i] = new Laser((int) x, (int) y, gameScreen);;
            lArray[i].setPosition(player.x+x+(i*30), player.y+y);
            if(lasernumber==24)
            {
                lasernumber=0;
            }
            lArray[i].elapsedFrames=0+lasernumber;
            lasernumber++;
        }

        box = new Rectangle((int) lArray[0].x, (int) lArray[0].y, (int) Scale.getScaledSizeX(1920)-lArray[0].x, (int) lArray[0].height);

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
            box.set((int) lArray[0].x, (int) lArray[0].y, (int) Scale.getScaledSizeX(1920)-lArray[0].x, (int) lArray[0].height);
            lArray[i].update();
        }
        if (!started)
        {
            durationStart = System.currentTimeMillis();
            started = true;
        }

        timer = System.currentTimeMillis() - durationStart;

        outerloop:
        for (int i = 0; i < gameScreen.meteors.size(); i++) {
            if (Intersector.overlaps(gameScreen.meteors.get(i).box, this.box))
            {
                gameScreen.meteors.get(i).health -= 2;
                gameScreen.meteors.get(i).updateTexture();
                if(gameScreen.meteors.get(i).health<=0)
                {
                    gameScreen.explosions.add(new Explosion((int) gameScreen.meteors.get(i).x - 70, (int) (gameScreen.meteors.get(i).y - 20), (int) gameScreen.meteors.get(i).radius*2, (int) gameScreen.meteors.get(i).radius*2, gameScreen));
                    gameScreen.meteors.remove(i);
                    gameScreen.meteors.add(new Meteor(gameScreen));
                    player.score+=100;
                }
                break outerloop;
            }
        }

        outerloop:
        for(int i = 0; i<gameScreen.enemies.size(); i++)
        {
            for(int j = 0; j<gameScreen.enemies.get(i).getRockets().size(); j++)
            {
                if(Intersector.overlaps(this.box, gameScreen.enemies.get(i).getRockets().get(j).box))
                {
                    gameScreen.enemies.get(i).getRockets().remove(j);
                    break outerloop;
                }
            }
            if(Intersector.overlaps(this.box, gameScreen.enemies.get(i).box))
            {
                gameScreen.explosions.add(new Explosion((int) gameScreen.enemies.get(i).enemyX - 70, (int) (gameScreen.enemies.get(i).enemyY - 20),
                        (int) gameScreen.enemies.get(i).enemyWidth, (int) gameScreen.enemies.get(i).enemyHeight, gameScreen));
                gameScreen.enemies.remove(i);
                player.score+=500;
                break outerloop;
            }
        }

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

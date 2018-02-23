package me.spacegame.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;

import me.spacegame.animations.Explosion;
import me.spacegame.animations.PacMan;
import me.spacegame.gameobjects.Comet;
import me.spacegame.gameobjects.Player;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 23.02.2018.
 */

public class PowerUpComet extends PowerUp
{


    private boolean started;
    private float x;
    private float y;
    private float width;
    private float height;
    private static Texture cometTexture;
    private Circle box;
    private Comet comet0;
    private Comet comet1;
    private Comet comet2;
    private int length = 300;
    private float deg0 = 90;
    private float deg1 = 89.5f;
    private float deg2 = 89.2f;


    public PowerUpComet(Player player, GameScreen gameScreen)
    {
        super(player, gameScreen);
        duration = 7000;
        texture = gameScreen.getGame().getTexture("pwupCometIcon");
        comet0 = new Comet(player);
        comet1 = new Comet(player);
        comet2 = new Comet(player);

        comet1.radius = player.width/4;
        comet2.radius = player.width/6;

        gameScreen.comets.add(comet0);
        gameScreen.comets.add(comet1);
        gameScreen.comets.add(comet2);
    }

    @Override
    public void start()
    {

    }

    @Override
    public void stop()
    {

    }

    @Override
    public void draw(SpriteBatch batch)
    {
        for(Comet c : gameScreen.comets)
        {
            c.draw(batch);
        }
    }

    @Override
    public boolean update()
    {
        if (!started)
        {
            durationStart = System.currentTimeMillis();
            started = true;
        }

        for(Comet c : gameScreen.comets)
        {
            c.update();
        }

        deg0+=0.08;
        deg1+=0.08;
        deg2+=0.08;

        timer = System.currentTimeMillis() - durationStart;
        if ((timer) < duration)
        {
            x = player.x+player.width/2;
            y = player.y+player.height/2;

            comet0.x = (float) (x + (length*Math.cos(deg0)));
            comet0.y = (float) (y + (length*Math.sin(deg0)));

            comet1.x = (float) (x + (length*Math.cos(deg1)));
            comet1.y = (float) (y + (length*Math.sin(deg1)));

            comet2.x = (float) (x + (length*Math.cos(deg2)));
            comet2.y = (float) (y + (length*Math.sin(deg2)));

            comet0.box.set(comet0.x-comet0.radius, comet0.y-comet0.radius, comet0.radius);
            comet1.box.set(comet0.x-comet0.radius, comet0.y-comet0.radius, comet0.radius);
            comet2.box.set(comet0.x-comet0.radius, comet0.y-comet0.radius, comet0.radius);
            return true;
        }
        gameScreen.explosions.add(new Explosion((int) (comet0.x - comet0.radius), (int) (comet0.y-comet0.radius), (int) comet0.radius*2, (int) comet0.radius*2, gameScreen));
        gameScreen.explosions.add(new Explosion((int) (comet1.x - comet1.radius), (int) (comet1.y-comet0.radius), (int) comet1.radius*2, (int) comet1.radius*2, gameScreen));
        gameScreen.explosions.add(new Explosion((int) (comet2.x - comet2.radius), (int) (comet2.y-comet0.radius), (int) comet2.radius*2, (int) comet2.radius*2, gameScreen));

        gameScreen.comets.clear();
        return false;
    }

    @Override
    public boolean isFinished()
    {
        return false;
    }

    @Override
    public void dispose()
    {

    }

    @Override
    public float getCooldown()
    {
        return (float) (timer/duration)*100.0f;
    }

}

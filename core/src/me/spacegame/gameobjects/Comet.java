package me.spacegame.gameobjects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import me.spacegame.animations.Explosion;
import me.spacegame.screens.GameScreen;
import me.spacegame.util.Scale;

/**
 * Created by Michi on 23.02.2018.
 */

public class Comet
{
    public static Texture texture;
    private boolean firstInit = false;

    public float x;
    public float y;
    public float radius;
    public Circle box;
    private int speed;
    public int damage;
    private Player player;
    private GameScreen gameScreen;

    public Comet(Player p)
    {
        player = p;
        gameScreen = player.getGameScreen();
        if(!firstInit)
        {
            texture = player.getGameScreen().getGame().getTexture("comet");
        }

        x = player.x+(player.width/2)+100;
        y = player.y+(player.height/2)+100;

        radius = player.width/2;
        speed = (int) Scale.getScaledSizeX(18);

        damage = 100;

        box = new Circle(x-radius, y-radius, radius);
    }


    public void draw(SpriteBatch batch)
    {
        batch.draw(texture, x-radius, y-radius, radius*2, radius*2);
    }

    public void update()
    {
        outerloop:
        for (int i = 0; i < gameScreen.meteors.size(); i++) {
            if (Intersector.overlaps(gameScreen.meteors.get(i).box, this.box))
            {
                gameScreen.explosions.add(new Explosion((int) gameScreen.meteors.get(i).x - 70, (int) (gameScreen.meteors.get(i).y - 20), (int) gameScreen.meteors.get(i).radius*2, (int) gameScreen.meteors.get(i).radius*2, gameScreen));
                gameScreen.meteors.remove(i);
                //gameScreen.meteors.add(new Meteor(gameScreen));
                gameScreen.game.getSound("explosion1sound").play(gameScreen.game.soundVolume);
                player.score+=100;

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
                gameScreen.game.getSound("explosion1sound").play(gameScreen.game.soundVolume);
                player.score+=500;
                break outerloop;
            }
        }
    }

    public static void dispose()
    {
        texture.dispose();
    }
}


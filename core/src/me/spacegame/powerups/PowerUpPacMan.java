package me.spacegame.powerups;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import me.spacegame.animations.Explosion;
import me.spacegame.animations.PacMan;
import me.spacegame.gameobjects.Enemy;
import me.spacegame.gameobjects.EnemyRocket;
import me.spacegame.gameobjects.Meteor;
import me.spacegame.gameobjects.Player;
import me.spacegame.screens.GameScreen;

/**
 * Created by Michi on 01.07.2017.
 */

public class PowerUpPacMan extends PowerUp {

    private boolean started;
    private float x;
    private float y;
    private float width;
    private float height;
    private static Texture pacManTexture;
    private Rectangle box;

    private PacMan pm;


    public PowerUpPacMan(Player player, GameScreen gameScreen) {
        super(player, gameScreen);
        duration = 6000;
        x = player.x-50;
        y = player.y-50;
        width = player.width+100;
        height = player.height+130;
        texture = gameScreen.getGame().getTexture("pwupPacManIcon");
        pm = new PacMan((int) x, (int) y, gameScreen);;
        box = new Rectangle(x, y, width, height);

    }

    @Override
    public void draw(SpriteBatch batch)
    {
        if ((timer) < duration)
        {
            pm.draw(batch);
            //batch.draw(pacManTexture, x, y, width, height);
        }
    }

    @Override
    public boolean update() {
        pm.update();
        if (!started)
        {
            durationStart = System.currentTimeMillis();
            player.setVisible(false);
            started = true;
        }

        timer = System.currentTimeMillis() - durationStart;
        if ((timer) < duration)
        {
            x = player.x - 50;
            y = player.y - 50;
            pm.setPosition( (int) x, (int) y, (int) width, (int) height);
            box.setPosition(x, y);



            outerloop:
            for (int i = 0; i < gameScreen.meteors.size(); i++) {
                if (Intersector.overlaps(gameScreen.meteors.get(i).box, this.box))
                {
                    gameScreen.explosions.add(new Explosion((int) gameScreen.meteors.get(i).x - 70, (int) (gameScreen.meteors.get(i).y - 20), gameScreen));
                    gameScreen.meteors.remove(i);
                    gameScreen.meteors.add(new Meteor(gameScreen));
                    //healPlayer(1);
                    //System.out.println("healed" + " : "+player.health);
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
                        healPlayer(20);
                        break outerloop;
                    }
                }
                if(Intersector.overlaps(this.box, gameScreen.enemies.get(i).box))
                {
                    gameScreen.explosions.add(new Explosion((int) gameScreen.enemies.get(i).enemyX - 70, (int) (gameScreen.enemies.get(i).enemyY - 20), gameScreen));
                    gameScreen.enemies.remove(i);
                    healPlayer(30);
                    break outerloop;
                }
            }

            return true;
        }

        player.setVisible(true);
        return false;
    }

    private void healPlayer(int heal)
    {
        player.health -= heal;
        gameScreen.healthBar.setHealth(player.health, false);
    }

    @Override
    public void start() {

    }

    @Override
    public void stop() {

    }

    @Override
    public boolean isFinished() {
        return false;
    }

    @Override
    public void dispose() {

    }
}

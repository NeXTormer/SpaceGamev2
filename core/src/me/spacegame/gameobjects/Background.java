package me.spacegame.gameobjects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Felix on 09-May-17.
 */

public class Background {

    private Texture bg;

    private int speed = 3;
    private int x = 0;

    public Background(String path)
    {
        bg = new Texture(Gdx.files.internal(path));
    }


    public void render(float delta, SpriteBatch batch)
    {
        x -= 200 * delta;

        if(x <= -bg.getWidth())
        {
            x = 0;
        }

        batch.draw(bg, x, 0);
        batch.draw(bg, x - (bg.getWidth()), 0);

    }

    public void dispose()
    {
        bg.dispose();
    }


}

package me.spacegame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Felix on 19-May-17.
 */

public class HealthBar {

    private Texture mainTexture;

    public HealthBar()
    {
        mainTexture = new Texture(Gdx.files.internal("ui/healthbarmain.png"));

    }

    public void draw(float delta, SpriteBatch batch)
    {

    }

}

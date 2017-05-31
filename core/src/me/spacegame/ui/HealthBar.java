package me.spacegame.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by Felix on 19-May-17.
 */

public class HealthBar {

    private Texture mainTexture;
    private FileHandle fragShader;


    public HealthBar()
    {

        fragShader = Gdx.files.internal("shader/questionmark.frag");
        System.out.println(fragShader.readString());
        //mainTexture = new Texture(Gdx.files.internal("ui/healthbarmain.png"));

    }

    public void draw(float delta, SpriteBatch batch)
    {

    }

}

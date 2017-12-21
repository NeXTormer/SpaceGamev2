package me.spacegame.util;

import com.badlogic.gdx.Gdx;

/**
 * Created by Felix on 21/12/2017.
 */

public class Scale {

    static float height = Gdx.graphics.getHeight();
    static float width = Gdx.graphics.getWidth();

    public static float getScaledSizeX(float defaultSize)
    {
        return (defaultSize / 1080.0f) * height;
    }

    public static float getScaledSizeY(float defaultSize)
    {
        return (defaultSize / 1920.0f) * width;
    }

}

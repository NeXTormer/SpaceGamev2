package me.spacegame.ui.menu;

import com.badlogic.gdx.Gdx;

/**
 * Created by Felix on 19-Jun-17.
 */

public class EmptyMenu extends MenuTemplate {


    public EmptyMenu(MenuManager menuManager) { super(menuManager); }

    @Override
    public void create() { }

    @Override
    public MenuTemplate activate()
    {
        Gdx.input.setInputProcessor(menuManager.getGameScreen().getInputMultiplexer());
        return this;
    }

    @Override
    public void draw() { }

    @Override
    public void dispose() { }
}
